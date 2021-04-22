package program.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import program.controllers.AuthorizationController;
import program.models.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static program.utils.HttpClass.PostStatisticsRequest;

public class RestAPI {

    private static final String SERVER_URL = "http://localhost:8080";
    private static final String SERVER_GET_USERS = SERVER_URL + "/user";
    private static final String AUTH = SERVER_URL + "/login";
    private static final String SERVER_GET_LESSONS = SERVER_URL + "/lesson";
    private static final String SERVER_GET_QUESTIONS = SERVER_URL + "/question";
    private static final String SERVER_GET_STATISTICS = SERVER_URL + "/statistic";
    private static final String SERVER_GET_ANSWER_HISTORIES = SERVER_URL + "/answer_history";

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
        String incorrect1 = null;
        try {
            incorrect1 = thisQuestion.get("incorrect1").getAsString();
        } catch (UnsupportedOperationException e){}
        String incorrect2 = null;
        try {
            incorrect2 = thisQuestion.get("incorrect2").getAsString();
        } catch (UnsupportedOperationException e){}
        String incorrect3 = null;
        try {
            incorrect3 = thisQuestion.get("incorrect3").getAsString();
        } catch (UnsupportedOperationException e){}
        Lesson lesson = parseLesson(thisQuestion.get("lessonId").getAsJsonObject());

        return new Question(id, text, correctAnswer, incorrect1, incorrect2, incorrect3, description, lesson);
    }
    
    public Statistics parseStatistics(JsonObject thisStatistics) throws ParseException {
        Integer id = thisStatistics.get("id").getAsInt();
        LocalDate date = DateUtil.parse(thisStatistics.get("date").getAsString());
        Integer score = thisStatistics.get("score").getAsInt();
        User user = parseUser(thisStatistics.get("user").getAsJsonObject());
        Lesson lesson = parseLesson(thisStatistics.get("lesson").getAsJsonObject());

        return new Statistics(id, date, score, user, lesson);
    }

    public AnswerHistory parseAnswerHistory(JsonObject thisAnswerHistory) throws ParseException {
        Integer id = thisAnswerHistory.get("id").getAsInt();
        boolean isCorrect = thisAnswerHistory.get("isCorrect").getAsBoolean();
        Statistics statistics = parseStatistics(thisAnswerHistory.get("statistic").getAsJsonObject());
        Question question = parseQuestion(thisAnswerHistory.get("question").getAsJsonObject());

        return new AnswerHistory(id, isCorrect, statistics, question);
    }

    public Map<String, User> auth(AuthorizationController authorizationController) {
        String responseString = HttpClass.PostRequest(AUTH, authorizationController.toJson());
        User responseUser = new User();
        try {
            JsonObject stringToParse = JsonParser.parseString(responseString).getAsJsonObject();
            responseUser = parseUser(stringToParse);
            responseString = stringToParse.get("token").getAsString();
        } catch (JsonSyntaxException e){ }
        User finalResponseUser = responseUser;
        String finalResponseString = responseString;
        Map<String, User> responseMap = new HashMap<>(){{
            put(finalResponseString, finalResponseUser);
        }};
        return responseMap;
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

    public void postUser(User newUser) throws UnirestException {

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

    public boolean deleteQuestion(Question question, String token){
        Integer id = question.getId();
        if (id == null){
            return false;
        }
        return HttpClass.DeleteRequest(SERVER_GET_QUESTIONS + "/" + id, token);
    }

    public void postQuestion(Question question, String token) throws JSONException {

        HttpClass.PostRequest(SERVER_GET_QUESTIONS, question.toJson(), token);
    }

    public void putQuestion(Question question, String token) throws JSONException {
        String jsonString = question.toJson();
        HttpClass.PutRequest(SERVER_GET_QUESTIONS + "/" + question.getId(), jsonString, token);
    }

    public Statistics postStatistics(Statistics statistics, String token) throws JSONException, ParseException, UnirestException {
        String jsonString = statistics.toJson();
        HttpResponse<String> response = PostStatisticsRequest(SERVER_GET_STATISTICS, jsonString, token);
        JsonObject jsonResult = JsonParser.parseString(response.getBody()).getAsJsonObject();
        return parseStatistics(jsonResult);
    }

    public void postAnswerHistory(AnswerHistory answerHistory, String token) throws JSONException {
        HttpClass.PostRequest(SERVER_GET_ANSWER_HISTORIES, answerHistory.toJson(), token);
    }

    public JsonArray jsonRes(String token, Lesson lesson, User user){
        JsonArray jsonResult = new JsonArray();

        if (lesson != null && user != null) {
            String buffer = HttpClass.getRequest(SERVER_GET_STATISTICS +
                    "?userLogin=" + user.getLogin() +
                    "&lessonId=" + lesson.getId(), token);
            jsonResult = JsonParser.parseString(buffer).getAsJsonArray();
        } else if (lesson == null && user != null){
            String buffer = HttpClass.getRequest(SERVER_GET_STATISTICS +
                    "?userLogin=" + user.getLogin(), token);
            jsonResult = JsonParser.parseString(buffer).getAsJsonArray();
        } else if (lesson != null && user == null){
            String buffer = HttpClass.getRequest(SERVER_GET_STATISTICS +
                    "?lessonId=" + lesson.getId(), token);
            jsonResult = JsonParser.parseString(buffer).getAsJsonArray();
        }
        return jsonResult;
    }

    public List<Statistics> getStatiscticsByLessonAndUser(String token, Lesson lesson, User user){
        List<Statistics> result = new ArrayList<>();
        try {
            JsonArray jsonResult = jsonRes(token, lesson, user);

            for (int i = 0; i < jsonResult.size(); i++) {
                JsonObject thisStatistics = jsonResult.get(i).getAsJsonObject();
                Statistics statistics = parseStatistics(thisStatistics);
                result.add(statistics);
            }
            return result;
        } catch (NullPointerException | ParseException e){
            return result;
        }
    }

    public List<AnswerHistory> getAnswerHistoryByStatistics(String token, Statistics statistics){
        List<AnswerHistory> result = new ArrayList<>();
        try {
            String buffer = HttpClass.getRequest(SERVER_GET_ANSWER_HISTORIES +
                    "?statisticsId=" + statistics.getId(), token);

            JsonArray jsonResult = JsonParser.parseString(buffer).getAsJsonArray();

            for (int i = 0; i < jsonResult.size(); i++) {
                JsonObject thisAnswerHistory = jsonResult.get(i).getAsJsonObject();
                AnswerHistory answerHistory = parseAnswerHistory(thisAnswerHistory);
                result.add(answerHistory);
            }
            return result;
        } catch (NullPointerException | ParseException e){
            return result;
        }
    }
}
