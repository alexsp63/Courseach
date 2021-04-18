package program.models;

import com.google.gson.Gson;
import javafx.beans.property.*;

import java.util.HashMap;
import java.util.Map;

public class Lesson implements JSONSerialize{

    private SimpleObjectProperty<Integer> id;
    private StringProperty name;
    private StringProperty text;
    private StringProperty questionType;

    public Lesson(){
        this(null, null, null, null);
    }

    public Lesson(String name, String text, String questionType){
        this.name = new SimpleStringProperty(name);
        this.text = new SimpleStringProperty(text);
        this.questionType = new SimpleStringProperty(questionType);
        this.id = null;
    }

    public Lesson(String name, String text, String questionType, Integer id){
        this.name = new SimpleStringProperty(name);
        this.text = new SimpleStringProperty(text);
        this.questionType = new SimpleStringProperty(questionType);
        this.id = new SimpleObjectProperty<Integer>(id);
    }

    public int getId() {
        return id.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getTextText() {
        return text.get();
    }

    public void setTextText(String text) {
        this.text.set(text);
    }

    public String getQuestionType() {
        return questionType.get();
    }

    public void setQuestionType(String questionType) {
        this.questionType.set(questionType);
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", name=" + name +
                ", text=" + text +
                ", questionType=" + questionType +
                '}';
    }

    @Override
    public String toJson() {
        Map<String, String> map = new HashMap<>();

        map.put("name", name.getValue());
        map.put("text", text.getValue());
        map.put("questionType", questionType.getValue());
        Gson gson = new Gson();
        return gson.toJson(map);
    }
}
