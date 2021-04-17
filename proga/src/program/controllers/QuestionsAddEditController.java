package program.controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import program.Main;
import program.models.Lesson;
import program.models.Question;
import program.utils.RestAPI;

public class QuestionsAddEditController {

    @FXML
    private TextField questionText;

    @FXML
    private TextField questionDescription;

    @FXML
    private TextField questionCorrect;

    @FXML
    private TextField questionIncorrect1;

    @FXML
    private TextField questionIncorrect2;

    @FXML
    private TextField questionIncorrect3;

    @FXML
    private Label message;

    private Stage questionStage;
    private Main main;
    private RestAPI restAPI;
    private String token;
    private Question question;
    private Lesson lesson;
    private boolean saveIsClicked;


    public void setStage(Stage questionStage, Main main, RestAPI restAPI, String token, Lesson lesson){
        this.questionStage = questionStage;
        this.main = main;
        this.restAPI = restAPI;
        this.token = token;
        this.lesson = lesson;

        if (lesson.getQuestionType().equals("Открытый вопрос")){
            questionIncorrect1.setVisible(false);
            questionIncorrect2.setVisible(false);
            questionIncorrect3.setVisible(false);
        } else if (lesson.getQuestionType().equals("2 варианта ответа")){
            questionIncorrect2.setVisible(false);
            questionIncorrect3.setVisible(false);
        }
    }

    public void setQuestion(Question question){
        this.question = question;
        if (question != null){
            questionText.setText(question.getText());
            questionDescription.setText(question.getDescription());
            questionCorrect.setText(question.getCorrectAnswer());
            if (lesson.getQuestionType().equals("2 варианта ответа")){
                questionIncorrect1.setText(question.getIncorrect1());
            } else if (lesson.getQuestionType().equals("4 варианта ответа")){
                questionIncorrect1.setText(question.getIncorrect1());
                questionIncorrect2.setText(question.getIncorrect2());
                questionIncorrect3.setText(question.getIncorrect3());
            }
        } else {
            questionText.setText("");
            questionDescription.setText("");
            questionCorrect.setText("");
            if (lesson.getQuestionType().equals("2 варианта ответа")){
                questionIncorrect1.setText("");
            } else if (lesson.getQuestionType().equals("4 варианта ответа")){
                questionIncorrect1.setText("");
                questionIncorrect2.setText("");
                questionIncorrect3.setText("");
            }
        }
    }

    public boolean isSaveIsClicked() {
        return saveIsClicked;
    }

    @FXML
    private void initialize(){}

    public String errorMessage(){
        if (questionText.getText() == null || questionText.getText().length() == 0 || questionText.getText().equals("")){
            return "Введите текст вопроса!";
        } else if (questionDescription.getText() == null || questionDescription.getText().length() == 0 || questionDescription.getText().equals("")){
            return "Введите пояснение к ответу!";
        } else if (questionCorrect.getText() == null || questionCorrect.getText().length() == 0 || questionCorrect.getText().equals("")){
            return "Введите правильный ответ!";
        }
        if (lesson.getQuestionType().equals("2 варианта ответа")) {
            if (questionIncorrect1.getText() == null || questionIncorrect1.getText().length() == 0 || questionIncorrect1.getText().equals("")) {
                return "Введите неправильный вариант ответа!";
            }
        }
        if (lesson.getQuestionType().equals("4 варианта ответа")) {
            if (questionIncorrect1.getText() == null || questionIncorrect1.getText().length() == 0 || questionIncorrect1.getText().equals("")) {
                return "Введите первый неправильный вариант ответа!";
            } else if (questionIncorrect2.getText() == null || questionIncorrect2.getText().length() == 0 || questionIncorrect2.getText().equals("")) {
                return "Введите второй неправильный вариант ответа!";
            } else if (questionIncorrect3.getText() == null || questionIncorrect3.getText().length() == 0 || questionIncorrect3.getText().equals("")) {
                return "Введите третий неправильный вариант ответа!";
            }
        }
        return "";
    }

    public boolean inputCheck(){
        String erMessage = errorMessage();
        if (erMessage.length() == 0) {
            return true;
        } else {
            message.setTextFill(Color.web("red"));
            message.setText(erMessage);
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(e -> message.setText(""));
            pause.play();
            return false;
        }
    }

    @FXML
    public void saveQ(){
        if (inputCheck()){
            question.setText(questionText.getText());
            question.setDescription(questionDescription.getText());
            question.setCorrectAnswer(questionCorrect.getText());
            if (lesson.getQuestionType().equals("Открытый вопрос")){
            } else if (lesson.getQuestionType().equals("2 варианта ответа")){
                question.setIncorrect1(questionIncorrect1.getText());
            } else {
                question.setIncorrect1(questionIncorrect1.getText());
                question.setIncorrect2(questionIncorrect2.getText());
                question.setIncorrect3(questionIncorrect3.getText());
            }
            question.setLesson(lesson);

            saveIsClicked = true;
            questionStage.close();
        }
    }

    @FXML
    public void cancelQ(){
        questionStage.close();
    }
}
