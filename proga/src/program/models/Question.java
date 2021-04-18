package program.models;

import javafx.beans.property.*;
import org.json.JSONException;
import org.json.JSONObject;

public class Question{

    private SimpleObjectProperty<Integer> id;
    private StringProperty text;
    private StringProperty correctAnswer;
    private StringProperty description;
    private StringProperty incorrect1;
    private StringProperty incorrect2;
    private StringProperty incorrect3;
    private Lesson lesson;
    private SimpleObjectProperty<String> type;

    public Question(){
        this(null, null, null, null, null, null, new Lesson());
    }

    public Question(String text, String correctAnswer, String description, Lesson lesson){
        //создание вопроса с открытым ответом
        this.id = null;
        this.text = new SimpleStringProperty(text);
        this.correctAnswer = new SimpleStringProperty(correctAnswer);
        this.description = new SimpleStringProperty(description);
        this.incorrect1 = null;
        this.incorrect2 = null;
        this.incorrect3 = null;
        this.lesson = lesson;
        this.type = new SimpleObjectProperty<>(lesson.getQuestionType());
    }

    public Question(Integer id, String text, String correctAnswer, String description, Lesson lesson){
        //для получаения вопроса с открытым ответом
        this.id = new SimpleObjectProperty<Integer>(id);
        this.text = new SimpleStringProperty(text);
        this.correctAnswer = new SimpleStringProperty(correctAnswer);
        this.description = new SimpleStringProperty(description);
        this.incorrect1 = null;
        this.incorrect2 = null;
        this.incorrect3 = null;
        this.lesson = lesson;
        this.type = new SimpleObjectProperty<>(lesson.getQuestionType());
    }

    public Question(String text, String correctAnswer, String incorrect1, String description, Lesson lesson){
        //создание вопроса с двумя вариантами ответа
        this.id = null;
        this.text = new SimpleStringProperty(text);
        this.correctAnswer = new SimpleStringProperty(correctAnswer);
        this.description = new SimpleStringProperty(description);
        this.incorrect1 = new SimpleStringProperty(incorrect1);
        this.incorrect2 = null;
        this.incorrect3 = null;
        this.lesson = lesson;
        this.type = new SimpleObjectProperty<>(lesson.getQuestionType());
    }

    public Question(Integer id, String text, String correctAnswer, String incorrect1, String description, Lesson lesson){
        //получение вопроса с двумя вариантами ответа
        this.id = new SimpleObjectProperty<Integer>(id);
        this.text = new SimpleStringProperty(text);
        this.correctAnswer = new SimpleStringProperty(correctAnswer);
        this.description = new SimpleStringProperty(description);
        this.incorrect1 = new SimpleStringProperty(incorrect1);
        this.incorrect2 = null;
        this.incorrect3 = null;
        this.lesson = lesson;
        this.type = new SimpleObjectProperty<>(lesson.getQuestionType());
    }

    public Question(String text, String correctAnswer, String incorrect1, String incorrect2, String incorrect3, String description, Lesson lesson){
        //создание вопроса с четырьмя вариантами ответа
        this.id = null;
        this.text = new SimpleStringProperty(text);
        this.correctAnswer = new SimpleStringProperty(correctAnswer);
        this.description = new SimpleStringProperty(description);
        this.incorrect1 = new SimpleStringProperty(incorrect1);
        this.incorrect2 = new SimpleStringProperty(incorrect2);
        this.incorrect3 = new SimpleStringProperty(incorrect3);
        this.lesson = lesson;
        this.type = new SimpleObjectProperty<>(lesson.getQuestionType());

    }

    public Question(Integer id, String text, String correctAnswer, String incorrect1, String incorrect2, String incorrect3, String description, Lesson lesson){
        //получение вопроса с четырьмя вариантами ответа
        this.id = new SimpleObjectProperty<Integer>(id);
        this.text = new SimpleStringProperty(text);
        this.correctAnswer = new SimpleStringProperty(correctAnswer);
        this.description = new SimpleStringProperty(description);
        this.incorrect1 = new SimpleStringProperty(incorrect1);
        this.incorrect2 = new SimpleStringProperty(incorrect2);
        this.incorrect3 = new SimpleStringProperty(incorrect3);
        this.lesson = lesson;
        this.type = new SimpleObjectProperty<>(lesson.getQuestionType());
    }

    public int getId() {
        return id.get();
    }

    public StringProperty textProperty() {
        return text;
    }

    public String getCorrectAnswer() {
        return correctAnswer.get();
    }

    public String getIncorrect1() {
        return incorrect1.get();
    }

    public String getIncorrect2() {
        return incorrect2.get();
    }

    public String getIncorrect3() {
        return incorrect3.get();
    }

    public String getText() {
        return text.get();
    }

    public String getDescription() {
        return description.get();
    }

    public String getType() {
        return type.get();
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer.set(correctAnswer);
    }

    public void setIncorrect1(String incorrect1) {
        this.incorrect1.set(incorrect1);
    }

    public void setIncorrect2(String incorrect2) {
        this.incorrect2.set(incorrect2);
    }

    public void setIncorrect3(String incorrect3) {
        this.incorrect3.set(incorrect3);
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", text=" + text +
                ", correctAnswer=" + correctAnswer +
                ", description=" + description +
                ", incorrect1=" + incorrect1 +
                ", incorrect2=" + incorrect2 +
                ", incorrect3=" + incorrect3 +
                ", lesson=" + lesson.toString() +
                '}';
    }

    public String toJson() throws JSONException {
        JSONObject map = new JSONObject();
        map.put("id", id.getValue());
        map.put("text", text.getValue());
        map.put("correctAnswer", correctAnswer.getValue());
        map.put("description", description.getValue());
        map.put("incorrect1", incorrect1.getValue());
        map.put("incorrect2", incorrect2.getValue());
        map.put("incorrect3", incorrect3.getValue());
        JSONObject less = new JSONObject();
        less.put("id", lesson.getId());
        less.put("name", lesson.getName());
        less.put("text", lesson.getTextText());
        less.put("questionType", lesson.getQuestionType());
        map.put("lessonId", less);
        return map.toString();
    }
}
