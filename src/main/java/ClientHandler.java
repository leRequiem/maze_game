import Maze_work.*;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private String clientName;
    private Maze maze;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {

                JSONObject clientRequestJSON = new JSONObject(clientMessage);
                String command = clientRequestJSON.getString("command");
                MazeCoords start = new MazeCoords(MazeCreator.getStartX(), MazeCreator.getStartY());
                MazeCoords exit = new MazeCoords(MazeCreator.getEndX(), MazeCreator.getEndY());
                String minSteps = String.valueOf(PathFinder.shortestPathToExit(Server.getMaze(), start, exit));

                if (command.equals("start")) {
                    clientName = clientRequestJSON.getString("clientName");
                    System.out.println("Игрок " + clientName + " начал игру");

                    int[][] mazeMatrix = Server.getMaze();
                    maze = new Maze(mazeMatrix, MazeCreator.getStartX(), MazeCreator.getStartY(), MazeCreator.getEndX(), MazeCreator.getEndY());
                    out.print(JsonRequests.statusStart(clientName, MazeCreator.getStartX(), MazeCreator.getStartY()).toString() + "\n");
                } else if (command.equals("direction")) {
                    String direction = clientRequestJSON.getString("direction");
                    if (maze.exitWasReached() && maze.move(direction)) {
                        // save to rating
                        out.print(JsonRequests.statusStop(String.valueOf(maze.getSteps()), minSteps).toString() + "\n");
                    } else {
                        out.print(JsonRequests.statusGo(maze.move(direction)).toString() + "\n");
                    }
                } else if (command.equals("stop")) {
                    out.print(JsonRequests.statusStop(String.valueOf(maze.getSteps()), minSteps).toString() + "\n");
                    break;
                } else {
                    // Неизвестная команда
                    JSONObject response = new JSONObject();
                    response.put("error", "Неизвестная команда");
                    out.write(response.toString() + "\n");
                    out.flush();
                }
            }
            clientSocket.close();

        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
