import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

public class JsonRequests {

    // Все JSON запросы отправляемые клиенту сервером
    public static JSONObject statusStart(String clientName, int startX, int startY) {
        int[] temp = new int[]{startX, startY};
        JSONArray startPoint = new JSONArray(temp);
        JSONObject responseJSON = new JSONObject();
        responseJSON.put("status", "start");
        responseJSON.put("message", "Привет, " + clientName);
        responseJSON.put("startPoint", startPoint);
        responseJSON.put("rating", "");
        return responseJSON;
    }

    public static JSONObject statusGo(boolean moveIsPossible) {
        JSONObject responseJSON = new JSONObject();
        responseJSON.put("status", "go");
        responseJSON.put("result", moveIsPossible ? 0 : 1);
        return responseJSON;
    }

    public static JSONObject statusStop(String steps, String minSteps, String filename) {
        JSONObject responseJSON = new JSONObject();
        JSONArray ratingJSON = convertRatingToJSON(filename);

        responseJSON.put("status", "stop");
        responseJSON.put("result", steps);
        responseJSON.put("min", minSteps);
        responseJSON.put("rating", ratingJSON);
        return responseJSON;
    }

    public static JSONObject wrongCommand() {
        JSONObject responseJSON = new JSONObject();
        responseJSON.put("error", "Неизвестная команда");
        return responseJSON;
    }

    public static JSONArray convertRatingToJSON(String filename) {
        JSONArray ratingJsonArray = new JSONArray();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)))) {
            String line;
            JSONObject jsonObject = new JSONObject();
            String userName;
            String steps;
            String minSteps;

            while ((line = reader.readLine()) != null) {
                String[] lineParts = line.split(",");
                if (lineParts.length == 3) {
                    userName = lineParts[0];
                    steps = lineParts[1];
                    minSteps = lineParts[2];

                    jsonObject.put("name", userName);
                    jsonObject.put("steps", steps);
                    jsonObject.put("min", minSteps);
                }
                ratingJsonArray.put(jsonObject);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return ratingJsonArray;
    }

    public static String JsonArrayToString(JSONArray jsonArray) {
        StringBuilder rating = new StringBuilder();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String userName = jsonObject.getString("name");
            String steps = jsonObject.getString("steps");
            String minSteps = jsonObject.getString("min");

            rating.append("Имя: ")
                    .append(userName)
                    .append(", Кол-во шагов: ")
                    .append(steps)
                    .append(", Минимальное кол-во шагов: ")
                    .append(minSteps)
                    .append("\n");
        }
        return rating.toString();
    }

    // все JSON запросы отправлемые серверу клиентом
    public static JSONObject commandStart(String clientName) {
        JSONObject responseJSON = new JSONObject();
        responseJSON.put("command", "start");
        responseJSON.put("clientName", clientName);
        return responseJSON;
    }

    public static JSONObject commandDirection(String direction) {
        JSONObject responseJSON = new JSONObject();
        responseJSON.put("command", "direction");
        responseJSON.put("direction", direction);
        return responseJSON;
    }

    public static JSONObject commandStop() {
        JSONObject responseJSON = new JSONObject();
        responseJSON.put("command", "stop");
        return responseJSON;
    }
}
