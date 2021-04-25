package program.models;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.json.JSONException;
import org.json.JSONObject;
import program.utils.DateUtil;

import java.time.LocalDate;

/**
 * Модель статистики
 */
public class Statistics {

    private SimpleObjectProperty<Integer> id;
    private SimpleObjectProperty<LocalDate> date;
    private SimpleObjectProperty<Integer> score;
    private User user;
    private Lesson lesson;

    /**
     * Пустой конструктор
     */
    public Statistics(){
        this(null, null, null, new User(), new Lesson());
    }

    /**
     * Конструктор создания объекта статистики
     * @param date - дата
     * @param score - результат
     * @param user - пользователь
     * @param lesson - урок
     */
    public Statistics(LocalDate date, Integer score, User user, Lesson lesson){
        this.id = null;
        this.date = new SimpleObjectProperty<LocalDate>(date);
        this.score = new SimpleObjectProperty<Integer>(score);
        this.user = user;
        this.lesson = lesson;
    }

    /**
     * Конструктор получения объекта статистики от сервера
     * @param id - уникальный идентификатор
     * @param date - дата
     * @param score - результат
     * @param user - пользователь
     * @param lesson - урок
     */
    public Statistics(Integer id, LocalDate date, Integer score, User user, Lesson lesson){
        this.id = new SimpleObjectProperty<Integer>(id);
        this.date = new SimpleObjectProperty<LocalDate>(date);
        this.score = new SimpleObjectProperty<Integer>(score);
        this.user = user;
        this.lesson = lesson;
    }

    /**
     * Получение обертки даты прохождения теста
     * @return дату прохождения
     */
    public SimpleStringProperty dateProperty() {
        return new SimpleStringProperty(DateUtil.format(this.getDate()));
    }

    /**
     * Получение результата
     * @return результат
     */
    public SimpleStringProperty scoreProperty(){
        return new SimpleStringProperty(Integer.toString(this.getScore()*10) + "%");
    }

    /**
     * Получение названия урока
     * @return название урока
     */
    public SimpleStringProperty lessonProperty(){
        return new SimpleStringProperty(lesson.getName());
    }

    /**
     * Получение обертки логина юзера, создавшего данную статистику
     * @return логин юзера
     */
    public SimpleStringProperty userProperty(){
        return new SimpleStringProperty(user.getLogin());
    }

    /**
     * Устновка даты прохождения теста
     * @param date - дата прохождения теста
     */
    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    /**
     * Получение даты создания статистики
     * @return дата прохождения теста
     */
    public LocalDate getDate() {
        return date.get();
    }

    /**
     * Установка результата объекту статистики
     * @param score рузльтат
     */
    public void setScore(Integer score) {
        this.score.set(score);
    }

    /**
     * Получение результата статистики
     * @return результат
     */
    public Integer getScore() {
        return score.get();
    }

    /**
     * Установка пользователя объекту статистики
     * @param user - пользователь
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Получение пользователя объекта статистики
     * @return пользователь
     */
    public User getUser() {
        return user;
    }

    /**
     * установка урока объекту статистики
     * @param lesson - урок
     */
    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    /**
     * Получение урока объекта статистики
     * @return урок
     */
    public Lesson getLesson() {
        return lesson;
    }

    /**
     * Получение уникального идентификатора
     * @return уникальный идентификатор
     */
    public Integer getId() {
        return id.get();
    }

    /**
     * Преобразование объекта статистики в строку
     * @return строку статистики
     */
    @Override
    public String toString() {
        return "Statistics{" +
                "id=" + id +
                ", date=" + date +
                ", score=" + score +
                ", user=" + user +
                ", lesson=" + lesson +
                '}';
    }

    /**
     * Преобразование объекта статистики в json-строку для отправки на сервер
     * @return json-строку
     * @throws JSONException - исключение
     */
    public String toJson() throws JSONException {
        JSONObject map = new JSONObject();

        map.put("id", id.getValue());
        map.put("date", DateUtil.format(date.getValue()));
        map.put("score", score.getValue());

        JSONObject us = new JSONObject();
        us.put("login", user.getLogin());
        us.put("password", user.getPassword());
        us.put("firstName", user.getFirstName());
        us.put("lastName", user.getLastName());
        us.put("role", user.getRole());
        us.put("status", user.getStatus());

        map.put("user", us);

        JSONObject less = new JSONObject();
        less.put("id", lesson.getId());
        less.put("name", lesson.getName());
        less.put("text", lesson.getTextText());
        less.put("questionType", lesson.getQuestionType());

        map.put("lesson", less);

        return map.toString();
    }
}
