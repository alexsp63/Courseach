package program.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import program.models.User;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class RestAPI {

    private static final String SERVER_URL = "http://localhost:8080";
    private static final String SERVER_GET_USERS = SERVER_URL + "/user";
    private static final String AUTH = SERVER_URL + "/login";


    public String auth(String login, String password) {
        try {
            return HttpClass.PostRequest(AUTH + "?username=" + URLEncoder.encode(login, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "Unsupported Encoding";
        }
    }

    public List<User> getUsers(){
        List<User> result = new ArrayList<>();
        String buffer = HttpClass.getRequest(SERVER_GET_USERS);
        JsonArray jsonResult = JsonParser.parseString(buffer).getAsJsonArray();

        for (int i=0; i<jsonResult.size(); i++){
            JsonObject thisUser = jsonResult.get(i).getAsJsonObject();
            String login = thisUser.get("login").getAsString();
            String password = thisUser.get("password").getAsString();
            String firstName = thisUser.get("firstName").getAsString();
            String lastName = thisUser.get("lastName").getAsString();
            String role = thisUser.get("role").getAsString();
            String status = thisUser.get("status").getAsString();

            User newUser = new User(login, password, firstName, lastName, role, status);
            result.add(newUser);
        }
        return result;
    }

    public List<String> getAllLogins(){
        List<String> logins = new ArrayList<>();
        String buffer = HttpClass.getRequest(SERVER_GET_USERS + "/logins");
        JsonArray jsonResult = JsonParser.parseString(buffer).getAsJsonArray();
        for (int i=0; i<jsonResult.size(); i++) {
            logins.add(jsonResult.get(i).getAsString());
        }
        return logins;
    }

    public void postUser(User newUser){
        HttpClass.PostRequest(SERVER_GET_USERS, newUser.toJson());
    }


}
