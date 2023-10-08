import org.json.*;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Gamer {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int serverPort = 20001;

        try (Socket socket = new Socket(host, serverPort);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Для начала игры напишите свое имя: ");
            String inputLine = scanner.nextLine();

            if (inputLine.equals("stop")) {
                out.print(JsonRequests.commandStop() + "\n");
            } else
                out.print(JsonRequests.commandStart(inputLine).toString() + "\n");

            // Читаем ответ сервера
            String serverResponse = in.readLine();
            JSONObject serverResponseJSON = new JSONObject(serverResponse);

            String status = serverResponseJSON.getString("status");
            if (status.equals("start")) {
                System.out.println(serverResponseJSON.getString("message"));
                int[] startPoint = toIntArray(serverResponseJSON.getJSONArray("startPoint"));

                // Указываем направление движения
                while (true) {
                    System.out.println("Введите направление движения(u - вверх, r - вправо, d - вниз, l - влево)");
                    String moveDirection = scanner.nextLine();

                    if (moveDirection.equals("stop")) {
                        out.print(JsonRequests.commandStop().toString() + "\n");
                    }

                    while (true) {
                        if (moveDirection.equals("u") || moveDirection.equals("r") || moveDirection.equals("d") || moveDirection.equals("l")) {
                            out.print(JsonRequests.commandDirection(moveDirection) + "\n");
                            break;
                        } else {
                            System.out.println("Неизвестная команда: " + moveDirection);
                        }
                    }

                    serverResponse = in.readLine();
                    serverResponseJSON = new JSONObject(serverResponse);

                    if (serverResponseJSON.getString("status").equals("go")) {
                        int moveResult = serverResponseJSON.getInt("result");

                        if (moveResult == 1)
                            System.out.println("Невозможно сделать ход, так как в том направлении стена");
                        else
                            System.out.println("Ход был сделан успешно");
                    }

                    else if (serverResponseJSON.getString("status").equals("stop")) {

                    }
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