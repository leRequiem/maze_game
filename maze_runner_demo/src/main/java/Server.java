import Maze_work.MazeCreator;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int SERVER_PORT = 1111;
    private static MazeCreator mazeCreator;
    // если ошибка доступа, то указать абсолютный путь
    private static final String filepath = "maze_runner_demo/src/main/resources/overallRating.txt";

    public static void main(String[] args) {
        mazeCreator = new MazeCreator(7, 7);

        try (ServerSocket server = new ServerSocket(SERVER_PORT)) {
            clearLogFile(filepath);

            System.out.println("Ожидание клиента...");

            while (true) {
                Socket clientSocket = server.accept();
                System.out.println("Клиент присоединился");

                ClientHandler clientHandler = new ClientHandler(clientSocket, filepath);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static int[][] getMaze() {
        return mazeCreator.getMaze();
    }


    // Метод для очистки файла
    private static void clearLogFile(String filePath) {
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
