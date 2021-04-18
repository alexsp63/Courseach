package program.controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import program.Main;
import program.models.Lesson;
import program.utils.RestAPI;

import static program.controllers.SignUpController.isDouble;
import static program.controllers.SignUpController.isInteger;

public class LessonAddEditController {

    @FXML
    private TextField lessonNameText;

    @FXML
    private TextField lessonTextText;

    @FXML
    private ComboBox<String> lessonQuestionType;

    @FXML
    private Label errorMessage;

    private Stage lessonStage;
    private Main main;
    private RestAPI restAPI;
    private boolean saveIsClicked;
    private String token;
    private Lesson lesson;

    public void setStage(Stage lessonStage, Main main, RestAPI restAPI, String token){
        this.lessonStage = lessonStage;
        this.main = main;
        this.restAPI = restAPI;
        this.token = token;
    }

    @FXML
    private void initialize() {
        lessonQuestionType.getItems().addAll("2 варианта ответа",
                                                  "4 варианта ответа",
                                                  "Открытый вопрос");
    }

    public boolean isSaveIsClicked() {
        return saveIsClicked;
    }

    public void setLesson(Lesson lesson){
        this.lesson = lesson;
        if (lesson != null){
            lessonNameText.setText(lesson.getName());
            lessonTextText.setText(lesson.getTextText());
            lessonQuestionType.setPromptText(lesson.getQuestionType());
            if (restAPI.getQuestionsByLesson(token, this.lesson).size() != 0){
                lessonQuestionType.setDisable(true); //если уже есть вопросы, удовлетворяющие одному типу, то нельзя его менять
            }
        } else {
            lessonQuestionType.setDisable(false);
            lessonNameText.setText("");
            lessonTextText.setText("");
            lessonQuestionType.setPromptText("");
        }
    }

    private String errorMessage(){
        if (lessonNameText.getText() == null || lessonNameText.getText().length() == 0){
            return "Недопустимое название урока!";
        }
        if (lessonTextText.getText() == null || isDouble(lessonTextText.getText()) == true
                || isInteger(lessonTextText.getText()) == true || lessonTextText.getText().length() == 0){
            return "Недопустимый текст урока!";
        }
        if ((lessonQuestionType.getValue() == null || lessonQuestionType.getValue().length() == 0) && lesson.getQuestionType() == null){
            return "Выберите тип вопросов из списка!";
        }
        return "";
    }

    public boolean inputCheck(){
        String message = errorMessage();
        if (message.length() == 0) {
            return true;
        } else {
            errorMessage.setTextFill(Color.web("red"));
            errorMessage.setText(message);
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(e -> errorMessage.setText(""));
            pause.play();
            return false;
        }
    }

    @FXML
    public void write(){
        if (inputCheck()){
            lesson.setName(lessonNameText.getText());
            lesson.setTextText(lessonTextText.getText());
            if (lesson.getQuestionType() == null || restAPI.getQuestionsByLesson(token, lesson).size() == 0) {
                lesson.setQuestionType(lessonQuestionType.getValue());
            }

            saveIsClicked = true;
            lessonStage.close();
        }
    }

    @FXML
    public void cancel(){
        lessonStage.close();
    }
}
