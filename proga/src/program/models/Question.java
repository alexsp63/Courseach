package program.models;

import javafx.beans.property.*;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Модель вопроса
 */
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

    /**
     * Пустой конструктор для создания нового объекта вопроса
     */
    public Question(){
        this(null, null, null, null, null, null, new Lesson());
    }

    /**
     * Формирование вопроса для отправки на сервер
     * @param text - текст вопроса
     * @param correctAnswer - верный ответ
     * @param incorrect1 - неверный вариант ответа
     * @param incorrect2 - неверный вариант ответа
     * @param incorrect3 - неверный вариант ответа
     * @param description - пояснение к вопросу
     * @param lesson - урок, в котором будет создать этот вопрос
     */
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

    /**
     * Конструктор для получения вопроса с четырьмя вариантами ответа с сервера
     * @param id - идентификатор вопроса
     * @param text - текст вопроса
     * @param correctAnswer - верный ответ
     * @param incorrect1 - неверный ответ
     * @param incorrect2 - неверный ответ
     * @param incorrect3 - неверный ответ
     * @param description - пояснение
     * @param lesson - урок
     */
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

    /**
     * Получение идентификатора вопроса
     * @return id вопроса
     */
    public int getId() {
        return id.get();
    }

    /**
     * Получение обёртки текста вопроса
     * @return текст вопроса
     */
    public StringProperty textProperty() {
        return text;
    }

    /**
     * Получение верного ответа на вопрос
     * @return правильный ответ на вопрос
     */
    public String getCorrectAnswer() {
        return correctAnswer.get();
    }

    /**
     * Получение неверного ответа на вопрос
     * @return неправильный ответ на вопрос
     */
    public String getIncorrect1() {
        return incorrect1.get();
    }

    /**
     * Получение неверного ответа на вопрос
     * @return неправильный ответ на вопрос
     */
    public String getIncorrect2() {
        return incorrect2.get();
    }

    /**
     * Получение неверного ответа на вопрос
     * @return неправильный ответ на вопрос
     */
    public String getIncorrect3() {
        return incorrect3.get();
    }

    /**
     * Получение текста вопроса
     * @return текст вопроса
     */
    public String getText() {
        return text.get();
    }

    /**
     * Получение пояснения к вопросу
     * @return пояснение к вопросу
     */
    public String getDescription() {
        return description.get();
    }

    /**
     * Получение типы вопроса
     * @return тип вопроса
     */
    public String getType() {
        return type.get();
    }

    /**
     * Получение урока, в котором находится этот вопрос
     * @return получение урока
     */
    public Lesson getLesson() {
        return lesson;
    }

    /**
     * Установка текста вопроса
     * @param text - текст вопроса
     */
    public void setText(String text) {
        this.text.set(text);
    }

    /**
     * Установка пояснения к вопросу
     * @param description - пояснение к вопросу
     */
    public void setDescription(String description) {
        this.description.set(description);
    }

    /**
     * Установка верного ответа на вопрос
     * @param correctAnswer - правильный ответ на вопрос
     */
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer.set(correctAnswer);
    }

    /**
     * Установка вопросу неправильного ответа
     * @param incorrect1 - неправильный ответ
     */
    public void setIncorrect1(String incorrect1) {
        this.incorrect1.set(incorrect1);
    }

    /**
     * Установка вопросу неправильного ответа
     * @param incorrect2 - неправильный ответ
     */
    public void setIncorrect2(String incorrect2) {
        this.incorrect2.set(incorrect2);
    }

    /**
     * Установка вопросу неправильного ответа
     * @param incorrect3 - неправильный ответ
     */
    public void setIncorrect3(String incorrect3) {
        this.incorrect3.set(incorrect3);
    }

    /**
     * Установка вопросу урока, в котором он будет отображаться
     * @param lesson - урок
     */
    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    /**
     * Преобразование вопроса в строку
     * @return строка вопроса
     */
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

    /**
     * Преобразование вопроса в json-строку для отправки на сервер
     * @return json-строку
     * @throws JSONException - исключение
     */
    public String toJson() throws JSONException {
        JSONObject map = new JSONObject();

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
