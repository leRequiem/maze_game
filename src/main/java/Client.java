import org.json.*;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class    Client {
    public static void main(String[] args) throws IOException {
        String host = "127.0.0.1";
        int serverPort = 20001;

        try (Socket socket = new Socket(host, serverPort);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Для начала игры напишите свое имя: ");
            String inputLine = scanner.nextLine();

            if (inputLine.equals("stop")) {
                out.println(JsonRequests.commandStop());
            } else
                out.println(JsonRequests.commandStart(inputLine).toString());

            // Читаем ответ сервера
            String serverResponse = in.readLine();
            JSONObject serverResponseJSON = new JSONObject(serverResponse);
            System.out.println(serverResponseJSON);
            String status = serverResponseJSON.getString("status");

            if (status.equals("start")) {
                System.out.println(serverResponseJSON.getString("message"));
                int[] startPoint = toIntArray(serverResponseJSON.getJSONArray("startPoint"));

                // Указываем направление движения
                while (true) {
                    System.out.println("Введите направление движения(u - вверх, r - вправо, d - вниз, l - влево)");
                    String moveDirection = scanner.nextLine();

                    if (moveDirection.equals("stop")) {
                        out.println(JsonRequests.commandStop().toString());
                    }
                    out.println(JsonRequests.commandDirection(moveDirection));

                    serverResponse = in.readLine();
                    serverResponseJSON = new JSONObject(serverResponse);

                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static int[] toIntArray(JSONArray jsonArray) {
        int[] result = new int[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            result[i] = jsonArray.getInt(i);
        }
        return result;
    }
}