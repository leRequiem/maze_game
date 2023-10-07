import Maze_files.MazeCreator;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private String clientName;

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

                if (command.equals("start")) {
                    clientName = clientRequestJSON.getString("clientName");
                    System.out.println("Игрок " + clientName + " начал игру");
                    out.println(JsonRequests.statusStart(clientName, MazeCreator.getStartX(), MazeCreator.getStartY()).toString());
                } else if (command.equals("direction")) {
                    String direction = clientRequestJSON.getString("direction");

                } else if (command.equals("stop")) {
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
