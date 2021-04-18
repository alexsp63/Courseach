package program.models;

import javafx.beans.property.SimpleObjectProperty;
import org.json.JSONException;
import org.json.JSONObject;
import program.utils.DateUtil;

import java.time.LocalDate;

public class Statistics {

    private SimpleObjectProperty<Integer> id;
    private SimpleObjectProperty<LocalDate> date;
    private SimpleObjectProperty<Integer> score;
    private User user;
    private Lesson lesson;

    public Statistics(){
        this(null, null, null, new User(), new Lesson());
    }

    public Statistics(LocalDate date, Integer score, User user, Lesson lesson){
        this.id = null;
        this.date = new SimpleObjectProperty<LocalDate>(date);
        this.score = new SimpleObjectProperty<Integer>(score);
        this.user = user;
        this.lesson = lesson;
    }

    public Statistics(Integer id, LocalDate date, Integer score, User user, Lesson lesson){
        this.id = new SimpleObjectProperty<Integer>(id);
        this.date = new SimpleObjectProperty<LocalDate>(date);
        this.score = new SimpleObjectProperty<Integer>(score);
        this.user = user;
        this.lesson = lesson;
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public LocalDate getDate() {
        return date.get();
    }

    public void setScore(Integer score) {
        this.score.set(score);
    }

    public Integer getScore() {
        return score.get();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public Integer getId() {
        return id.get();
    }

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
