package program.models;

import javafx.beans.property.SimpleObjectProperty;
import org.json.JSONException;
import org.json.JSONObject;
import program.utils.DateUtil;

/**
 * Модель истории ответов
 */
public class AnswerHistory {

    private SimpleObjectProperty<Integer> id;
    private SimpleObjectProperty<Boolean> isCorrect;
    private Statistics statistics;
    private Question question;

    /**
     * Пустой конструктор, используется для создания нового объекта истории ответов
     */
    public AnswerHistory(){
        this(null, false, new Statistics(), new Question());
    }

    /**
     * Конструктор, создающий историю ответов для изменения
     * @param isCorrect - правильный ли ответ дан на вопрос
     * @param statistics - статистика, к которой эта история ответов принадлежит
     * @param question - вопрос, на который ответили правильно или неправильно
     */
    public AnswerHistory(boolean isCorrect, Statistics statistics, Question question){
        this.id = null;
        this.isCorrect = new SimpleObjectProperty<Boolean>(isCorrect);
        this.statistics = statistics;
        this.question = question;
    }

    /**
     * Конструктор, используемый для создания объекта истории ответов при получении его от сервера
     * @param id - уникальный идентификатор
     * @param isCorrect - правильный ли ответ был дан
     * @param statistics - статистика, которой принадлежит эта история ответов
     * @param question - вопрос, на который был дан ответ
     */
    public AnswerHistory(Integer id, boolean isCorrect, Statistics statistics, Question question){
        this.id = new SimpleObjectProperty<Integer>(id);
        this.isCorrect = new SimpleObjectProperty<Boolean>(isCorrect);
        this.statistics = statistics;
        this.question = question;
    }

    /**
     * Получение уникального идентификатора истории ответов
     * @return уникальный идентификатор
     */
    public Integer getId() {
        return id.get();
    }

    /**
     * Получение правильности ответа на вопрос
     * @return правильный ли был дан ответ
     */
    public Boolean getIsCorrect() {
        return isCorrect.get();
    }

    /**
     * Установка правильности ответа
     * @param isCorrect - правильный или неправильный ответ был дан
     */
    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect.set(isCorrect);
    }

    /**
     * Получение объекта статистики
     * @return объект статистики истории ответов
     */
    public Statistics getStatistics() {
        return statistics;
    }

    /**
     * Установка истории ответов статистики, в результате которой она была сформирована
     * @param statistics - объект статистики для установки
     */
    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    /**
     * Получение вопроса в истории ответов
     * @return объект вопроса
     */
    public Question getQuestion() {
        return question;
    }

    /**
     * Установка объекта вопроса
     * @param question - вопрос, который на установить
     */
    public void setQuestion(Question question) {
        this.question = question;
    }

    /**
     * Переводит объект в строку
     * @return строка с полями объекта
     */
    @Override
    public String toString() {
        return "AnswerHistory{" +
                "id=" + id +
                ", isCorrect=" + isCorrect +
                ", statistics=" + statistics +
                ", question=" + question +
                '}';
    }

    /**
     * Перевод в Json-строку
     * @return строку вида Json для добавления или обновления в базе данных
     * @throws JSONException - исключение
     */
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
