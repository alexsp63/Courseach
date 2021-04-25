package program.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONException;
import program.controllers.AuthorizationController;
import program.models.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static program.utils.HttpClass.PostStatisticsRequest;

/**
 * Класс для посылания запросов и интерпретации ответов
 */
public class RestAPI {

    private static final String SERVER_URL = "http://localhost:8080";
    private static final String SERVER_GET_USERS = SERVER_URL + "/user";
    private static final String AUTH = SERVER_URL + "/login";
    private static final String SERVER_GET_LESSONS = SERVER_URL + "/lesson";
    private static final String SERVER_GET_QUESTIONS = SERVER_URL + "/question";
    private static final String SERVER_GET_STATISTICS = SERVER_URL + "/statistic";
    private static final String SERVER_GET_ANSWER_HISTORIES = SERVER_URL + "/answer_history";

    /**
     * Преобразование объекта ответа в пользователя
     * @param thisUser - объект ответа
     * @return новый объект модели Пользователь
     */
    public User parseUser(JsonObject thisUser){
        String login = thisUser.get("login").getAsString();
        String password = thisUser.get("password").getAsString();
        String firstName = thisUser.get("firstName").getAsString();
        String lastName = thisUser.get("lastName").getAsString();
        String role = thisUser.get("role").getAsString();
        String status = thisUser.get("status").getAsString();
        return new User(login, password, firstName, lastName, role, status);
    }

    /**
     * Преобразование объекта ответа в урок
     * @param thisLesson - объект ответа
     * @return новый объект модели Урок
     */
    public Lesson parseLesson(JsonObject thisLesson){
        Integer id = thisLesson.get("id").getAsInt();
        String name = thisLesson.get("name").getAsString();
        String text = thisLesson.get("text").getAsString();
        String questionType = thisLesson.get("questionType").getAsString();

        return new Lesson(name, text, questionType, id);
    }

    /**
     * Преобразование объекта ответа в вопрос
     * @param thisQuestion - объект ответа
     * @return новый объект модели Вопрос
     */
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

    /**
     * Преобразование объекта ответа в статистику
     * @param thisStatistics - объект ответа
     * @return новый объект модели Статистика
     */
    public Statistics parseStatistics(JsonObject thisStatistics) throws ParseException {
        Integer id = thisStatistics.get("id").getAsInt();
        LocalDate date = DateUtil.parse(thisStatistics.get("date").getAsString());
        Integer score = thisStatistics.get("score").getAsInt();
        User user = parseUser(thisStatistics.get("user").getAsJsonObject());
        Lesson lesson = parseLesson(thisStatistics.get("lesson").getAsJsonObject());

        return new Statistics(id, date, score, user, lesson);
    }

    /**
     * Преобразование объекта ответа в историю ответов
     * @param thisAnswerHistory - объект ответа
     * @return новый объект модели История ответов
     */
    public AnswerHistory parseAnswerHistory(JsonObject thisAnswerHistory) throws ParseException {
        Integer id = thisAnswerHistory.get("id").getAsInt();
        boolean isCorrect = thisAnswerHistory.get("isCorrect").getAsBoolean();
        Statistics statistics = parseStatistics(thisAnswerHistory.get("statistic").getAsJsonObject());
        Question question = parseQuestion(thisAnswerHistory.get("question").getAsJsonObject());

        return new AnswerHistory(id, isCorrect, statistics, question);
    }

    /**
     * Словарь ответа при авторизации
     * @param authorizationController - то, что пытаюсь авторизовать и аутентифицировать
     * @return словарь ответа
     */
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

    /**
     * Список пользователй
     * @param token - токен текущего пользователя
     * @return список пользователей
     */
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


    /**
     * Получение всех занятых логинов при регистрации
     * @return список занятых логинов
     */
    public List<String> getAllLogins(){
        List<String> logins = new ArrayList<>();
        String buffer = HttpClass.getRequest(SERVER_GET_USERS + "/logins");
        JsonArray jsonResult = JsonParser.parseString(buffer).getAsJsonArray();
        for (int i=0; i<jsonResult.size(); i++) {
            logins.add(jsonResult.get(i).getAsString());
        }
        return logins;
    }

    /**
     * Добавление нового пользователя
     * @param newUser - пользователь, которого надо добавить
     * @throws UnirestException - исключение
     */
    public void postUser(User newUser) throws UnirestException {

        HttpClass.PostRequest(SERVER_GET_USERS, newUser.toJson());
    }

    /**
     * Обновление уже существующего пользователя
     * @param user - пользователь с параметрами, которые нужно обновить у текущего пользователя
     * @param token - токен пользователя
     */
    public void putUser(User user, String token){
        String jsonString = user.toJson();
        HttpClass.PutRequest(SERVER_GET_USERS + "/" + user.getLogin(), jsonString, token);
    }

    /**
     * Получение уроков
     * @param token - токен
     * @return список уроков
     */
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

    /**
     * Поиск вопросов по id урока
     * @param token - токен
     * @param lesson - урок
     * @return список вопросов данного урока
     */
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

    /**
     * Удаление вопроса
     * @param lesson - вопрос для удаления
     * @param token - токен пользователя
     * @return - результат удаления, да или нет
     */
    public boolean deleteLesson(Lesson lesson, String token){
        Integer id = lesson.getId();
        if (id == null){
            return false;
        }
        return HttpClass.DeleteRequest(SERVER_GET_LESSONS + "/" + id, token);
    }

    /**
     * Добавление урока
     * @param lesson - урок для добавления
     * @param token - токен пользователя
     */
    public void postLesson(Lesson lesson, String token){

        HttpClass.PostRequest(SERVER_GET_LESSONS, lesson.toJson(), token);
    }

    /**
     * Изменение вопроса существующего урока
     * @param lesson - урок для изменения
     * @param token - токен
     */
    public void putLesson(Lesson lesson, String token){
        String jsonString = lesson.toJson();
        HttpClass.PutRequest(SERVER_GET_LESSONS + "/" + lesson.getId(), jsonString, token);
    }

    /**
     * Удаление вопроса
     * @param question - вопрос для удаления
     * @param token - токен
     * @return удалось ли удалить
     */
    public boolean deleteQuestion(Question question, String token){
        Integer id = question.getId();
        if (id == null){
            return false;
        }
        return HttpClass.DeleteRequest(SERVER_GET_QUESTIONS + "/" + id, token);
    }

    /**
     * Добавление вопроса
     * @param question - вопрос для добавления
     * @param token - токен
     * @throws JSONException - исключение
     */
    public void postQuestion(Question question, String token) throws JSONException {

        HttpClass.PostRequest(SERVER_GET_QUESTIONS, question.toJson(), token);
    }

    /**
     * Обновление вопроса
     * @param question - вопрос для обновления
     * @param token - токен
     * @throws JSONException - исключение
     */
    public void putQuestion(Question question, String token) throws JSONException {
        String jsonString = question.toJson();
        HttpClass.PutRequest(SERVER_GET_QUESTIONS + "/" + question.getId(), jsonString, token);
    }

    /**
     * Добавление статистики
     * @param statistics - объект статистики для добавления
     * @param token - токен
     * @return добавленный объект статистики
     * @throws JSONException - исключение
     * @throws ParseException - исключение
     * @throws UnirestException - исключение
     */
    public Statistics postStatistics(Statistics statistics, String token) throws JSONException, ParseException, UnirestException {
        String jsonString = statistics.toJson();
        HttpResponse<String> response = PostStatisticsRequest(SERVER_GET_STATISTICS, jsonString, token);
        JsonObject jsonResult = JsonParser.parseString(response.getBody()).getAsJsonObject();
        return parseStatistics(jsonResult);
    }

    /**
     * Добавление истории ответов
     * @param answerHistory - история ответов для добавления
     * @param token - токен
     * @throws JSONException - исключение
     */
    public void postAnswerHistory(AnswerHistory answerHistory, String token) throws JSONException {
        HttpClass.PostRequest(SERVER_GET_ANSWER_HISTORIES, answerHistory.toJson(), token);
    }

    /**
     * Получение объекта статистики в виде JSON-массива
     * @param token - токен
     * @param lesson - урок для поиска статистики по нему
     * @param user - пользователь для поиска статистики по нему
     * @return Json-массива из объектов статистики, удовлетворяющих поиску
     */
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

    /**
     * Получение списка объектов статистики по уроку и пользователю
     * @param token - токен
     * @param lesson - урок
     * @param user - пользователь
     * @return список найденных объектов статистики
     */
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

    /**
     * Получение истории ответов по id статистики
     * @param token - токен
     * @param statistics - статистика, для которой надо искать историю ответов
     * @return список объектов истории ответов, удовлетворяющих поиску
     */
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
