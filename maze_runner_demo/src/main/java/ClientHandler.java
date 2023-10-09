import Maze_work.*;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private String clientName;
    private Maze maze;
    private String filepath;

    public ClientHandler(Socket clientSocket, String filepath) {
        this.clientSocket = clientSocket;
        this.filepath = filepath;
    }

    @Override
    public void run() {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             FileWriter fileWriter = new FileWriter(filepath, true)) {

            String clientMessage;
            label:
            while ((clientMessage = in.readLine()) != null) {

                JSONObject clientRequestJSON = new JSONObject(clientMessage);
                String command = clientRequestJSON.getString("command");
                MazeCoords start = new MazeCoords(MazeCreator.getStartX(), MazeCreator.getStartY());
                MazeCoords exit = new MazeCoords(MazeCreator.getEndX(), MazeCreator.getEndY());
                String minSteps = String.valueOf(PathFinder.shortestPathToExit(Server.getMaze(), new int[] {start.getX(), start.getY()}, new int[] {exit.getX(), exit.getY()}));

                switch (command) {
                    case "start":
                        clientName = clientRequestJSON.getString("clientName");
                        System.out.println("Игрок " + clientName + " начал игру");

                        int[][] mazeMatrix = Server.getMaze();
                        maze = new Maze(mazeMatrix, start.getX(), start.getY(), exit.getX(), exit.getY());
                        maze.drawMaze();

                        out.println(JsonRequests.statusStart(clientName, start.getX(), start.getY()).toString());
                        break;
                    case "direction":
                        String direction = clientRequestJSON.getString("direction");
                        boolean moveResult = maze.move(direction);

                        if (moveResult && maze.exitWasReached()) {
                            // сохраняем результат в файл с общим рейтингом
                            fileWriter.write(clientName + "," + maze.getSteps() + "," + minSteps + "\n");
                            fileWriter.flush();
                            out.println(JsonRequests.statusStop(String.valueOf(maze.getSteps()), minSteps, filepath).toString());
                            System.out.println("Игрок " + clientName + " завершил игру");
                        } else {
                            out.println(JsonRequests.statusGo(moveResult).toString());
                            System.out.println("Игрок " + clientName + " указал направление: " + direction);
                        }

                        break;
                    case "stop":
                        out.println(JsonRequests.statusStop(String.valueOf(maze.getSteps()), minSteps, filepath).toString());
                        System.out.println("Игрок " + clientName + " досрочно завершил игру");
                        break label;
                    default:
                        // Неизвестная команда
                        out.println(JsonRequests.wrongCommand().toString());
                        System.out.println("Игрок ввел неопознанную команду");
                        break;
                }
            }
            clientSocket.close();

        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
