package program.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import program.controllers.AuthorizationController;
import program.models.User;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RestAPI {

    private static final String SERVER_URL = "http://localhost:8080";
    private static final String SERVER_GET_USERS = SERVER_URL + "/user";
    private static final String AUTH = SERVER_URL + "/login";

    public User parseUser(JsonObject thisUser){
        String login = thisUser.get("login").getAsString();
        String password = thisUser.get("password").getAsString();
        String firstName = thisUser.get("firstName").getAsString();
        String lastName = thisUser.get("lastName").getAsString();
        String role = thisUser.get("role").getAsString();
        String status = thisUser.get("status").getAsString();
        return new User(login, password, firstName, lastName, role, status);
    }


    public String auth(AuthorizationController authorizationController) {
        return HttpClass.PostRequest(AUTH, authorizationController.toJson());
    }

    public List<User> getUsers(String token){
        List<User> result = new ArrayList<>();
        String buffer = HttpClass.getRequest(SERVER_GET_USERS, token);
        JsonArray jsonResult = JsonParser.parseString(buffer).getAsJsonArray();

        for (int i=0; i<jsonResult.size(); i++){
            JsonObject thisUser = jsonResult.get(i).getAsJsonObject();
            User newUser = parseUser(thisUser);
            result.add(newUser);
        }
        return result;
    }

    //это уже после авторизации
    public User getOneUser(String id, String token){
        String buffer = HttpClass.getRequest(SERVER_GET_USERS + "/" + id, token);
        JsonObject thisUser = JsonParser.parseString(buffer).getAsJsonObject();

        return parseUser(thisUser);
    }

    //это нужно для авторизации
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

    public void putUser(User user, String token){
        String jsonString = user.toJson();
        HttpClass.PutRequest(SERVER_GET_USERS + "/" + user.getLogin(), jsonString, token);
    }

}
