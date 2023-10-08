import org.json.JSONArray;
import org.json.JSONObject;

public class JsonRequests {

    public static JSONObject statusStart(String clientName, int startX, int startY) {
        int[] temp = new int[] {startX, startY};
        JSONArray startPoint = new JSONArray(temp);
        JSONObject responseJSON = new JSONObject();
        responseJSON.put("status", "start");
        responseJSON.put("message", "Привет, " + clientName);
        responseJSON.put("startPoint", startPoint);
        responseJSON.put("rating", "");
        return responseJSON;
    }

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

    public static JSONObject statusGo(boolean moveIsPossible) {
        JSONObject responseJSON = new JSONObject();
        responseJSON.put("status", "go");
        responseJSON.put("result", moveIsPossible? 0 : 1);
        return responseJSON;
    }

    public static JSONObject statusStop(String steps, String minSteps) {
        JSONObject responseJSON = new JSONObject();
        responseJSON.put("status", "stop");
        responseJSON.put("result", steps);
        responseJSON.put("min", minSteps);
        responseJSON.put("rating", "rating");
        return responseJSON;
    }
}
