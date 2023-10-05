import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int SERVER_PORT = 20001;

    public static void main(String[] args) {
        try(ServerSocket server = new ServerSocket(SERVER_PORT)) {

            System.out.println("Waiting for client...");

            while (true) {

                Socket clientSocket = server.accept();
                System.out.println("Client connected");

                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }


    private static class ClientHandler implements Runnable {

        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {

            try {
                OutputStream outputStream = clientSocket.getOutputStream();
                InputStream inputStream = clientSocket.getInputStream();

                String inputLine;
                while (true) {

                }
//                clientSocket.close();
//                System.out.println("Client disconnected");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}