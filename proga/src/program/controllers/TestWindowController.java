package program.controllers;

import javafx.animation.PauseTransition;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import program.Main;
import program.models.Lesson;
import program.models.Question;
import program.models.User;
import program.utils.RestAPI;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @FXML
    private void initialize(){

        openAnswer.setStyle("-fx-background-color: white");
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
//            button.setOnMouseEntered(event -> {
//                button.setStyle("-fx-background-color: grey");
//            });
//            button.setOnMouseExited(event -> button.setStyle(""));
            button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    compareAnswer(button.getText(), button);
                }
            });
        }
    }

    private void compareAnswer(String usersAnswer, Button clickedButton){
        if (usersAnswer == questionList.get(n).getCorrectAnswer()){
            score += 1;
            clickedButton.setStyle("-fx-background-color: #72ff72");
        } else {
            clickedButton.setStyle("-fx-background-color: #ff3737");
        }
        descriptionText.setText(questionList.get(n).getDescription());
        n += 1;
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    var1.setDisable(true);
                    var2.setDisable(true);
                    var3.setDisable(true);
                    var4.setDisable(true);
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if (n != 10) {
                    var1.setDisable(false);
                    var2.setDisable(false);
                    var3.setDisable(false);
                    var4.setDisable(false);
                    clickedButton.setStyle("-fx-background-color: #e0e0e0");
                    setTestQuestion();
                } else {
                    endTestButton.setDisable(false);
                    endTestButton.setVisible(true);
                }
            }
        });
        new Thread(sleeper).start();
    }

    private void compareAnswer(String usersAnswer){
        //для открытого ответа
        if (usersAnswer.equals(questionList.get(n).getCorrectAnswer())) {
            score += 1;
            openAnswer.setStyle("-fx-background-color: #72ff72");
        } else {
            openAnswer.setStyle("-fx-background-color: #ff3737");
        }
        descriptionText.setText(questionList.get(n).getDescription());
        n += 1;
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    openAnswer.setDisable(true);
                    nextQuestionButton.setDisable(true);
                    Thread.sleep(5000);
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
                    openAnswer.setStyle("-fx-background-color: white");
                    setTestQuestion();
                } else {

                    nextQuestionButton.setDisable(true);
                    nextQuestionButton.setVisible(false);
                    endTestButton.setDisable(false);
                    endTestButton.setVisible(true);
                }
            }
        });
        new Thread(sleeper).start();
    }

    private void setTestQuestion(){
        descriptionText.setText("");
        Question currentQuestion = questionList.get(n);
        questionText.setText(currentQuestion.getText());
        if (lesson.getQuestionType().equals("2 варианта ответа")){
            List<String> possibleAnswers = new ArrayList<>();
            possibleAnswers.add(currentQuestion.getCorrectAnswer());
            possibleAnswers.add(currentQuestion.getIncorrect1());
            Collections.shuffle(possibleAnswers);
            var1.setText(possibleAnswers.get(0));
            var2.setText(possibleAnswers.get(1));
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
        }
    }

    @FXML
    public void next(){
        compareAnswer(openAnswer.getText());
    }

    @FXML
    public void end(){
        questionText.setText("Тест закончен! Ваша оценка: " + score + "/" + questionList.size());
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

    @FXML
    public void close(){
        questionStage.close();
    }

}
