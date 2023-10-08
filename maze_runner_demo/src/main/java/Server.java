import Maze_work.MazeCreator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int SERVER_PORT = 20002;
    private static MazeCreator mazeCreator;

    public static void main(String[] args) {
        mazeCreator = new MazeCreator(7, 7);

        try (ServerSocket server = new ServerSocket(SERVER_PORT)) {

            System.out.println("Ожидание клиента...");

            while (true) {
                Socket clientSocket = server.accept();
                System.out.println("Клиент присоединился");

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static int[][] getMaze() {
        return mazeCreator.getMaze();
    }
}
