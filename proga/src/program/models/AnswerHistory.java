package program.models;

import javafx.beans.property.SimpleObjectProperty;
import org.json.JSONException;
import org.json.JSONObject;
import program.utils.DateUtil;

public class AnswerHistory {

    private SimpleObjectProperty<Integer> id;
    private SimpleObjectProperty<Boolean> isCorrect;
    private Statistics statistics;
    private Question question;

    public AnswerHistory(){
        this(null, false, new Statistics(), new Question());
    }

    public AnswerHistory(boolean isCorrect, Statistics statistics, Question question){
        this.id = null;
        this.isCorrect = new SimpleObjectProperty<Boolean>(isCorrect);
        this.statistics = statistics;
        this.question = question;
    }

    public AnswerHistory(Integer id, boolean isCorrect, Statistics statistics, Question question){
        this.id = new SimpleObjectProperty<Integer>(id);
        this.isCorrect = new SimpleObjectProperty<Boolean>(isCorrect);
        this.statistics = statistics;
        this.question = question;
    }

    public Integer getId() {
        return id.get();
    }

    public Boolean getIsCorrect() {
        return isCorrect.get();
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect.set(isCorrect);
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "AnswerHistory{" +
                "id=" + id +
                ", isCorrect=" + isCorrect +
                ", statistics=" + statistics +
                ", question=" + question +
                '}';
    }

    public String toJson() throws JSONException {
        JSONObject map = new JSONObject();

        map.put("id", id.getValue());
        map.put("isCorrect", isCorrect.getValue());

        JSONObject stat = new JSONObject();

        stat.put("id", statistics.getId());
        stat.put("date", DateUtil.format(statistics.getDate()));
        stat.put("score", statistics.getScore());

        JSONObject us = new JSONObject();
        us.put("login", statistics.getUser().getLogin());
        us.put("password", statistics.getUser().getPassword());
        us.put("firstName", statistics.getUser().getFirstName());
        us.put("lastName", statistics.getUser().getLastName());
        us.put("role", statistics.getUser().getRole());
        us.put("status", statistics.getUser().getStatus());

        stat.put("user", us);

        JSONObject less = new JSONObject();
        less.put("id", statistics.getLesson().getId());
        less.put("name", statistics.getLesson().getName());
        less.put("text", statistics.getLesson().getTextText());
        less.put("questionType", statistics.getLesson().getQuestionType());

        stat.put("lesson", less);

        map.put("statistic", stat);

        JSONObject ques = new JSONObject();
        ques.put("id", question.getId());
        ques.put("text", question.getText());
        ques.put("correctAnswer", question.getCorrectAnswer());
        ques.put("description", question.getDescription());
        ques.put("incorrect1", question.getIncorrect1());
        ques.put("incorrect2", question.getIncorrect2());
        ques.put("incorrect3", question.getIncorrect3());

        ques.put("lessonId", less);

        map.put("question", ques);

        return map.toString();
    }
}
