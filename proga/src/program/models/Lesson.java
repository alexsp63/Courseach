package program.models;

import com.google.gson.Gson;
import javafx.beans.property.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Модель урока
 */
public class Lesson implements JSONSerialize{

    private SimpleObjectProperty<Integer> id;
    private StringProperty name;
    private StringProperty text;
    private StringProperty questionType;

    /**
     * Пустой конструктор (для создания нового объекта урока)
     */
    public Lesson(){
        this(null, null, null, null);
    }

    /**
     * Конструктор
     * @param name - название урока
     * @param text - текст урока
     * @param questionType - тип вопросов урока
     */
    public Lesson(String name, String text, String questionType){
        this.name = new SimpleStringProperty(name);
        this.text = new SimpleStringProperty(text);
        this.questionType = new SimpleStringProperty(questionType);
        this.id = null;
    }

    /**
     * Конструктор при получении из базы данных
     * @param name - название урока
     * @param text - текст урока
     * @param questionType - тип вопросов в уроке
     * @param id - уникальный идентификатор урока
     */
    public Lesson(String name, String text, String questionType, Integer id){
        this.name = new SimpleStringProperty(name);
        this.text = new SimpleStringProperty(text);
        this.questionType = new SimpleStringProperty(questionType);
        this.id = new SimpleObjectProperty<Integer>(id);
    }

    /**
     * Получение id урока
     * @return id урока
     */
    public int getId() {
        return id.get();
    }

    /**
     * Обертка названия урока
     * @return название урока
     */
    public StringProperty nameProperty() {
        return name;
    }

    /**
     * Получение названия урока
     * @return название урока
     */
    public String getName() {
        return name.get();
    }

    /**
     * Установка названия урока
     * @param name - название урока
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     * Получение текста урока
     * @return текст урока
     */
    public String getTextText() {
        return text.get();
    }

    /**
     * Установка текста урока
     * @param text - текст урока
     */
    public void setTextText(String text) {
        this.text.set(text);
    }

    /**
     * Получение типов вопросов урока
     * @return тип вопросов в уроке
     */
    public String getQuestionType() {
        return questionType.get();
    }

    /**
     * Установка типов вопросов в уроке
     * @param questionType - тип вопросов в уроке
     */
    public void setQuestionType(String questionType) {
        this.questionType.set(questionType);
    }

    /**
     * Получение обертки типы вопросов в уроке
     * @return тип вопросов в уроке
     */
    public StringProperty questionTypeProperty() {
        return questionType;
    }

    /**
     * Представление урока в виде строки
     * @return строку урока
     */
    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", name=" + name +
                ", text=" + text +
                ", questionType=" + questionType +
                '}';
    }

    /**
     * Представление уроки в виде Json-строки для отправки на сервер
     * @return урок в виде json-строки
     */
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
