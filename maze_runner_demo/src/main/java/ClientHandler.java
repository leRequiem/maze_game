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

        String filename = "maze_runner_demo/src/main/resources/overallRating.txt";

        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             FileWriter fileWriter = new FileWriter(filename, true)) {

            clearLogFile(filename);

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
                    maze.drawMaze();

                    out.println(JsonRequests.statusStart(clientName, MazeCreator.getStartX(), MazeCreator.getStartY()).toString());
                } else if (command.equals("direction")) {
                    String direction = clientRequestJSON.getString("direction");
                    boolean moveResult = maze.move(direction);

                    if (moveResult && maze.exitWasReached()) {
                        // сохраняем результат в файл с общим рейтингом
                        fileWriter.write(clientName + "," + maze.getSteps() + "," + minSteps + "\n");
                        out.println(JsonRequests.statusStop(String.valueOf(maze.getSteps()), minSteps, filename).toString());
                        System.out.println("Игрок " + clientName + " завершил игру");
                    } else {
                        out.println(JsonRequests.statusGo(moveResult).toString());
                        System.out.println("Игрок " + clientName + " указал направление: " + direction);
                    }

                } else if (command.equals("stop")) {
                    out.println(JsonRequests.statusStop(String.valueOf(maze.getSteps()), minSteps, filename).toString());
                    System.out.println("Игрок " + clientName + " досрочно завершил игру");
                    break;
                } else {
                    // Неизвестная команда
                    out.println(JsonRequests.wrongCommand().toString());
                    System.out.println("Игрок ввел неопознанную команду");
                }
            }
            clientSocket.close();

        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    // Метод для очистки файла
    private void clearLogFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
