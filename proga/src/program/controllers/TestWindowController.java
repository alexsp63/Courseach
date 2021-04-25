package program.controllers;

import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.animation.*;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONException;
import program.Main;
import program.models.*;
import program.utils.RestAPI;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Контроллер, отвечающий за тестовое окно - форма testWindow.fxml
 */
public class TestWindowController {

    @FXML
    private Label questionText;

    @FXML
    private Label descriptionText;

    @FXML
    private TextField openAnswer;

    @FXML
    private Button var1;

    @FXML
    private Button var2;

    @FXML
    private Button var3;

    @FXML
    private Button var4;

    @FXML
    private Button nextQuestionButton;

    @FXML
    private Button endTestButton;

    @FXML
    private Button closeButton;

    @FXML
    private Label questNumb;

    private int n = 0;
    private int score = 0;

    private Main main;
    private RestAPI restAPI;
    private List<Question> questionList;

    //внешние ключи
    private User user;
    private Lesson lesson;

    private Stage questionStage;
    private String token;
    private Statistics statistics;
    private List<AnswerHistory> answerHistoryList;

    /**
     * Инициализация главных элементов формы, установка элементов в зависимости от типа вопросов
     * @param questionStage - окна теста
     * @param main - main
     * @param restAPI - restAPI
     * @param token - токен
     * @param questionList - список вопросов по выбранному пользователем уроку
     * @param user - пользователь, проходящий тест
     * @param lesson - урок, тест по которому проходит пользователь
     */
    public void setStage(Stage questionStage,
                                Main main,
                                RestAPI restAPI,
                                String token,
                                List<Question> questionList,
                                User user,
                                Lesson lesson){

        this.questionStage = questionStage;
        this.main = main;
        this.restAPI = restAPI;
        this.token = token;
        this.questionList = questionList;
        this.user = user;
        this.lesson = lesson;

        this.statistics = new Statistics();

        this.answerHistoryList = new ArrayList<>();
        for (int i=0; i<10; i++){
            this.answerHistoryList.add(new AnswerHistory());
        }

        closeButton.setVisible(false);
        closeButton.setDisable(true);

        nextQuestionButton.setDisable(true);
        nextQuestionButton.setVisible(false);
        endTestButton.setDisable(true);
        endTestButton.setVisible(false);

        if (lesson.getQuestionType().equals("Открытый вопрос")){
            nextQuestionButton.setVisible(true);
            openAnswer.setDisable(false);
            openAnswer.setVisible(true);
            var1.setDisable(true);
            var1.setVisible(false);
            var2.setDisable(true);
            var2.setVisible(false);
            var3.setDisable(true);
            var3.setVisible(false);
            var4.setDisable(true);
            var4.setVisible(false);
        } else if (lesson.getQuestionType().equals("2 варианта ответа")){
            openAnswer.setDisable(true);
            openAnswer.setVisible(false);
            var1.setDisable(false);
            var1.setVisible(true);
            var2.setDisable(false);
            var2.setVisible(true);
            var3.setDisable(true);
            var3.setVisible(false);
            var4.setDisable(true);
            var4.setVisible(false);
        } else {
            openAnswer.setDisable(true);
            openAnswer.setVisible(false);
            var1.setDisable(false);
            var1.setVisible(true);
            var2.setDisable(false);
            var2.setVisible(true);
            var3.setDisable(false);
            var3.setVisible(true);
            var4.setDisable(false);
            var4.setVisible(true);
        }

        setTestQuestion();
    }

    /**
     * Инициализация стилей тестовых элементов - кнопок и поля открытого ответа, задание им поведения
     */
    @FXML
    private void initialize(){

        openAnswer.setStyle("-fx-border-color: #14a9ff; -fx-border-width: 0 0 2 0;");
        openAnswer.textProperty().addListener( (observable, oldValue, newValue) -> {
            if (!newValue.equals("") && !(newValue == null) && !(newValue.length() == 0)){
                nextQuestionButton.setDisable(false);
            } else {
                nextQuestionButton.setDisable(true);
            }
        });

        Button[] buttons = {var1, var2, var3, var4};
        for (Button button: buttons){
            button.setStyle("-fx-background-color: #e0e0e0");
            button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    compareAnswer(button.getText(), button);
                }
            });
        }
    }

    /**
     * Обращение к серверу на добавление статистики в базу данных по завершении прохождения пользователем теста
     * @throws JSONException - исключение
     * @throws ParseException - исключение
     * @throws UnirestException - исключение
     */
    private void postThisStatistics() throws JSONException, ParseException, UnirestException {
        this.statistics.setDate(LocalDate.now());
        this.statistics.setScore(score);
        this.statistics.setUser(user);
        this.statistics.setLesson(lesson);
        Statistics statisticsForHistory = restAPI.postStatistics(this.statistics, token);
        for (AnswerHistory answerHistory: answerHistoryList){
            answerHistory.setStatistics(statisticsForHistory);
        }
        for (AnswerHistory answerHistory: answerHistoryList){
            restAPI.postAnswerHistory(answerHistory, token);
        }
    }

    /**
     * Сравнение выбранного варианта ответа с правильным для вопросов с кнопками
     * @param usersAnswer - ответ, выбранный пользователем
     * @param clickedButton - кнопку, на которую он нажал
     */
    private void compareAnswer(String usersAnswer, Button clickedButton) {
        AnswerHistory currentAnswerHistory = answerHistoryList.get(n);
        if (usersAnswer == questionList.get(n).getCorrectAnswer()){
            currentAnswerHistory.setIsCorrect(true);
            score += 1;
            clickedButton.setStyle("-fx-background-color: #72ff72");
        } else {
            currentAnswerHistory.setIsCorrect(false);
            clickedButton.setStyle("-fx-background-color: #ff5a5a");
        }
        descriptionText.setStyle("-fx-text-fill: blue");
        descriptionText.setText(questionList.get(n).getDescription());
        createAppearEffect(descriptionText);
        n += 1;
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Button[] buttons = {var1, var2, var3, var4};
                    for (Button button: buttons) {
                        button.setDisable(true);
                    }
                    Thread.sleep(4000);
                    if (n != 10) {
                        createDisappearEffect(questionText);
                        for (Button button : buttons) {
                            createDisappearEffect(button);
                        }
                        createDisappearEffect(descriptionText);
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if (n != 10) {
                    Button[] buttons = {var1, var2, var3, var4};
                    for (Button button: buttons) {
                        button.setDisable(false);
                    }
                    clickedButton.setStyle("-fx-background-color: #e0e0e0");
                    setTestQuestion();
                } else {
                    try {
                        postThisStatistics();
                    } catch (JSONException | ParseException | UnirestException e) {
                        e.printStackTrace();
                    }
                    endTestButton.setDisable(false);
                    endTestButton.setVisible(true);
                }
            }
        });
        new Thread(sleeper).start();
    }

    /**
     * Сравнение ответа, данного пользователем, на открытый вопрос
     * @param usersAnswer - ответ пользователя
     */
    private void compareAnswer(String usersAnswer) {
        //для открытого ответа
        AnswerHistory currentAnswerHistory = answerHistoryList.get(n);
        if (usersAnswer.equals(questionList.get(n).getCorrectAnswer())) {
            currentAnswerHistory.setIsCorrect(true);
            score += 1;
            openAnswer.setStyle("-fx-border-color: green; -fx-border-width: 0 0 4 0;");
        } else {
            currentAnswerHistory.setIsCorrect(false);
            openAnswer.setStyle("-fx-border-color: red; -fx-border-width: 0 0 4 0;");
        }
        descriptionText.setStyle("-fx-text-fill: blue");
        descriptionText.setText(questionList.get(n).getDescription());
        createAppearEffect(descriptionText);
        n += 1;
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    openAnswer.setDisable(true);
                    nextQuestionButton.setDisable(true);
                    Thread.sleep(4000);
                    if (n != 10) {
                        createDisappearEffect(questionText);
                        createDisappearEffect(descriptionText);
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if (n != 10) {
                    openAnswer.setDisable(false);
                    openAnswer.setText("");
                    openAnswer.setStyle("-fx-border-color: #14a9ff; -fx-border-width: 0 0 2 0;");
                    setTestQuestion();
                } else {
                    try {
                        postThisStatistics();
                    } catch (JSONException | ParseException | UnirestException e) {
                        e.printStackTrace();
                    }
                    nextQuestionButton.setDisable(true);
                    nextQuestionButton.setVisible(false);
                    endTestButton.setDisable(false);
                    endTestButton.setVisible(true);
                }
            }
        });
        new Thread(sleeper).start();
    }

    /**
     * Создание эффекта выцветания вариантов ответа
     * @param button - кнопка с выцветаемым текстом
     */
    public void createAppearEffect(Button button){
        KeyValue initKeyValue = new KeyValue(button.opacityProperty(), 0.0);
        KeyFrame initFrame = new KeyFrame(Duration.ZERO, initKeyValue);
        KeyValue endKeyValue = new KeyValue(button.opacityProperty(), 1.0);
        KeyFrame endFrame = new KeyFrame(Duration.seconds(1), endKeyValue);

        Timeline timeline = new Timeline(initFrame, endFrame);
        timeline.setCycleCount(1);
        timeline.play();
    }

    /**
     * Создание эффекта плавного выцветания текста вопроса и описания
     * @param label - плавно выцветающий label
     */
    public void createAppearEffect(Label label){
        KeyValue initKeyValue = new KeyValue(label.opacityProperty(), 0.0);
        KeyFrame initFrame = new KeyFrame(Duration.ZERO, initKeyValue);
        KeyValue endKeyValue = new KeyValue(label.opacityProperty(), 1.0);
        KeyFrame endFrame = new KeyFrame(Duration.seconds(1), endKeyValue);

        Timeline timeline = new Timeline(initFrame, endFrame);
        timeline.setCycleCount(1);
        timeline.play();
    }

    /**
     * Создание эффекта плавного исчезания для label'ов
     * @param label - тот, который должен исчезать
     */
    public void createDisappearEffect(Label label){
        KeyValue initKeyValue = new KeyValue(label.opacityProperty(), 1.0);
        KeyFrame initFrame = new KeyFrame(Duration.ZERO, initKeyValue);
        KeyValue endKeyValue = new KeyValue(label.opacityProperty(), 0.0);
        KeyFrame endFrame = new KeyFrame(Duration.seconds(1), endKeyValue);

        Timeline timeline1 = new Timeline(initFrame, endFrame);
        timeline1.setCycleCount(1);
        timeline1.play();
    }

    /**
     * Создание эффекта исчезновения для кнопок
     * @param button - кнопка, к которой применяется этот эффект
     */
    public void createDisappearEffect(Button button){
        KeyValue initKeyValue = new KeyValue(button.opacityProperty(), 1.0);
        KeyFrame initFrame = new KeyFrame(Duration.ZERO, initKeyValue);
        KeyValue endKeyValue = new KeyValue(button.opacityProperty(), 0.0);
        KeyFrame endFrame = new KeyFrame(Duration.seconds(1), endKeyValue);

        Timeline timeline1 = new Timeline(initFrame, endFrame);
        timeline1.setCycleCount(1);
        timeline1.play();
    }

    /**
     * Установка тестового вопроса
     */
    private void setTestQuestion(){
        questNumb.setText("Вопрос: " + (n+1) + "/10");
        descriptionText.setText("");
        createAppearEffect(descriptionText);
        Question currentQuestion = questionList.get(n);
        AnswerHistory currentAnswerHistory = answerHistoryList.get(n);
        currentAnswerHistory.setQuestion(currentQuestion);

        questionText.setText(currentQuestion.getText());

        if (lesson.getQuestionType().equals("2 варианта ответа")){
            List<String> possibleAnswers = new ArrayList<>();
            possibleAnswers.add(currentQuestion.getCorrectAnswer());
            possibleAnswers.add(currentQuestion.getIncorrect1());
            Collections.shuffle(possibleAnswers);
            var1.setText(possibleAnswers.get(0));
            var2.setText(possibleAnswers.get(1));
            Button[] allButtons = {var1, var2};
            for (Button button: allButtons) {
                createAppearEffect(button);
            }
        } else if (lesson.getQuestionType().equals("4 варианта ответа")){
            List<String> possibleAnswers = new ArrayList<>();
            possibleAnswers.add(currentQuestion.getCorrectAnswer());
            possibleAnswers.add(currentQuestion.getIncorrect1());
            possibleAnswers.add(currentQuestion.getIncorrect2());
            possibleAnswers.add(currentQuestion.getIncorrect3());
            Collections.shuffle(possibleAnswers);
            var1.setText(possibleAnswers.get(0));
            var2.setText(possibleAnswers.get(1));
            var3.setText(possibleAnswers.get(2));
            var4.setText(possibleAnswers.get(3));
            Button[] allButtons = {var1, var2, var3, var4};
            for (Button button: allButtons) {
                createAppearEffect(button);
            }
        }

        createAppearEffect(questionText);
    }

    /**
     * Нажатие кнопки перехода к следующему вопросу (для вопросов с открытым вариантом ответа)
     */
    @FXML
    public void next(){
        compareAnswer(openAnswer.getText().trim());
    }

    /**
     * Кнопка окончания теста
     */
    @FXML
    public void end(){
        questionText.setText("Тест закончен! Ваша оценка: " + score + "/" + questionList.size());
        createAppearEffect(questionText);
        openAnswer.setVisible(false);
        var1.setVisible(false);
        var2.setVisible(false);
        var3.setVisible(false);
        var4.setVisible(false);
        descriptionText.setVisible(false);
        endTestButton.setVisible(false);
        nextQuestionButton.setVisible(false);

        closeButton.setDisable(false);
        closeButton.setVisible(true);
    }

    /**
     * Досрочное закрытие окна тестирования
     */
    @FXML
    public void close(){
        questionStage.close();
    }

}
