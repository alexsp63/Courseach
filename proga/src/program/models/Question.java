package program.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Question {

    private IntegerProperty id;
    private StringProperty text;
    private StringProperty correctAnswer;
    private StringProperty description;
    private StringProperty incorrect1;
    private StringProperty incorrect2;
    private StringProperty incorrect3;
    private Lesson lesson;

    public Question(){
        this(null, null, null, null, null, null, null, null);
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
    }

    public Question(Integer id, String text, String correctAnswer, String description, Lesson lesson){
        //для получаения вопроса с открытым ответом
        this.id = new SimpleIntegerProperty(id);
        this.text = new SimpleStringProperty(text);
        this.correctAnswer = new SimpleStringProperty(correctAnswer);
        this.description = new SimpleStringProperty(description);
        this.incorrect1 = null;
        this.incorrect2 = null;
        this.incorrect3 = null;
        this.lesson = lesson;
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
    }

    public Question(Integer id, String text, String correctAnswer, String incorrect1, String description, Lesson lesson){
        //получение вопроса с двумя вариантами ответа
        this.id = new SimpleIntegerProperty(id);
        this.text = new SimpleStringProperty(text);
        this.correctAnswer = new SimpleStringProperty(correctAnswer);
        this.description = new SimpleStringProperty(description);
        this.incorrect1 = new SimpleStringProperty(incorrect1);
        this.incorrect2 = null;
        this.incorrect3 = null;
        this.lesson = lesson;
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
    }

    public Question(Integer id, String text, String correctAnswer, String incorrect1, String incorrect2, String incorrect3, String description, Lesson lesson){
        //получение вопроса с четырьмя вариантами ответа
        this.id = new SimpleIntegerProperty(id);
        this.text = new SimpleStringProperty(text);
        this.correctAnswer = new SimpleStringProperty(correctAnswer);
        this.description = new SimpleStringProperty(description);
        this.incorrect1 = new SimpleStringProperty(incorrect1);
        this.incorrect2 = new SimpleStringProperty(incorrect2);
        this.incorrect3 = new SimpleStringProperty(incorrect3);
        this.lesson = lesson;
    }

    public StringProperty textProperty() {
        return text;
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
}
