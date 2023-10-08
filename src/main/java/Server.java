import Maze_work.MazeCreator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    public static final int SERVER_PORT = 20001;
    private static final List<ClientHandler> clients = new ArrayList<>();
    private static MazeCreator mazeCreator;

    public static void main(String[] args) {
        mazeCreator = new MazeCreator(7, 7);

        try (ServerSocket server = new ServerSocket(SERVER_PORT)) {

            System.out.println("Ожизалние клиента...");

            while (true) {
                Socket clientSocket = server.accept();
                System.out.println("Клиент присоединился");

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
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
