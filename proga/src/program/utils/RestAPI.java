package program.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import program.controllers.AuthorizationController;
import program.models.Lesson;
import program.models.Question;
import program.models.User;

import java.util.ArrayList;
import java.util.List;

public class RestAPI {

    private static final String SERVER_URL = "http://localhost:8080";
    private static final String SERVER_GET_USERS = SERVER_URL + "/user";
    private static final String AUTH = SERVER_URL + "/login";
    private static final String SERVER_GET_LESSONS = SERVER_URL + "/lesson";
    private static final String SERVER_GET_QUESTIONS = SERVER_URL + "/question";

    public User parseUser(JsonObject thisUser){
        String login = thisUser.get("login").getAsString();
        String password = thisUser.get("password").getAsString();
        String firstName = thisUser.get("firstName").getAsString();
        String lastName = thisUser.get("lastName").getAsString();
        String role = thisUser.get("role").getAsString();
        String status = thisUser.get("status").getAsString();
        return new User(login, password, firstName, lastName, role, status);
    }

    public Lesson parseLesson(JsonObject thisLesson){
        Integer id = thisLesson.get("id").getAsInt();
        String name = thisLesson.get("name").getAsString();
        String text = thisLesson.get("text").getAsString();
        String questionType = thisLesson.get("questionType").getAsString();

        return new Lesson(name, text, questionType, id);
    }

    public Question parseQuestion(JsonObject thisQuestion){
        Integer id = thisQuestion.get("id").getAsInt();
        String text = thisQuestion.get("text").getAsString();
        String correctAnswer = thisQuestion.get("correctAnswer").getAsString();
        String description = thisQuestion.get("description").getAsString();
        String incorrect1 = thisQuestion.get("incorrect1").toString();
        String incorrect2 = thisQuestion.get("incorrect2").toString();
        String incorrect3 = thisQuestion.get("incorrect3").toString();
        Lesson lesson = parseLesson(thisQuestion.get("lessonId").getAsJsonObject());

        //System.out.println(new Question(id, text, correctAnswer, description, incorrect1, incorrect2, incorrect3, lesson));

        return new Question(id, text, correctAnswer, description, incorrect1, incorrect2, incorrect3, lesson);
    }

    public String auth(AuthorizationController authorizationController) {
        return HttpClass.PostRequest(AUTH, authorizationController.toJson());
    }



    public List<User> getUsers(String token){
        List<User> result = new ArrayList<>();
        try {
            String buffer = HttpClass.getRequest(SERVER_GET_USERS, token);
            JsonArray jsonResult = JsonParser.parseString(buffer).getAsJsonArray();

            for (int i = 0; i < jsonResult.size(); i++) {
                JsonObject thisUser = jsonResult.get(i).getAsJsonObject();
                User newUser = parseUser(thisUser);
                result.add(newUser);
            }
            return result;
        } catch (NullPointerException e){
            return result;
        }
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

    public List<Lesson> getLessons(String token){
        List<Lesson> result = new ArrayList<>();
        try {
            String buffer = HttpClass.getRequest(SERVER_GET_LESSONS, token);
            JsonArray jsonResult = JsonParser.parseString(buffer).getAsJsonArray();

            for (int i=0; i<jsonResult.size(); i++){
                JsonObject thisLesson = jsonResult.get(i).getAsJsonObject();
                Lesson newLesson = parseLesson(thisLesson);
                result.add(newLesson);
            }
            return result;
        } catch (NullPointerException e){
            return result;
        }
    }

    public List<Question> getQuestionsByLesson(String token, Lesson lesson){
        List<Question> result = new ArrayList<>();
        try {
            String buffer = HttpClass.getRequest(SERVER_GET_QUESTIONS + "?lessonId=" + lesson.getId(),
                    token);

            JsonArray jsonResult = JsonParser.parseString(buffer).getAsJsonArray();

            for (int i = 0; i < jsonResult.size(); i++) {
                JsonObject thisQuestion = jsonResult.get(i).getAsJsonObject();
                Question question = parseQuestion(thisQuestion);
                result.add(question);
            }

            return result;
        } catch (NullPointerException e){
            return result;
        }
    }

    public boolean deleteLesson(Lesson lesson, String token){
        Integer id = lesson.getId();
        if (id == null){
            return false;
        }
        return HttpClass.DeleteRequest(SERVER_GET_LESSONS + "/" + id, token);
    }

    public void postLesson(Lesson lesson, String token){

        HttpClass.PostRequest(SERVER_GET_LESSONS, lesson.toJson(), token);
    }

    public void putLesson(Lesson lesson, String token){
        String jsonString = lesson.toJson();
        HttpClass.PutRequest(SERVER_GET_LESSONS + "/" + lesson.getId(), jsonString, token);
    }
}
