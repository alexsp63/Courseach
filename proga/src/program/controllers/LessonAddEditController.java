package program.controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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
    private TextArea lessonTextText;

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

    public static void makeRed(TextField textField){
        textField.setStyle("-fx-border-color: red;");
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> textField.setStyle("-fx-border-color: #84cdff;"));
        pause.play();
    }

    public void makeRed(TextArea textArea){
        textArea.setStyle("-fx-border-color: red;");
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> textArea.setStyle("-fx-border-color: #84cdff;"));
        pause.play();
    }

    public void makeRed(ComboBox<String> comboBox){
        comboBox.setStyle("-fx-border-color: red;");
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> comboBox.setStyle("-fx-border-color: #bfd5ff;"));
        pause.play();
    }

    @FXML
    private void initialize() {

        lessonNameText.setOnMouseClicked(mouseEvent -> {
            lessonNameText.setStyle("-fx-border-color: #14a9ff;");
            lessonTextText.setStyle("-fx-border-color: #84cdff;");
            lessonQuestionType.setStyle("-fx-border-color: #bfd5ff;");
        });

        lessonTextText.setOnMouseClicked(mouseEvent -> {
            lessonTextText.setStyle("-fx-border-color: #14a9ff;");
            lessonNameText.setStyle("-fx-border-color: #84cdff;");
            lessonQuestionType.setStyle("-fx-border-color: #bfd5ff;");
        });

        lessonQuestionType.setOnMouseClicked(mouseEvent -> {
            lessonTextText.setStyle("-fx-border-color: #84cdff;");
            lessonNameText.setStyle("-fx-border-color: #84cdff;");
        });

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
            makeRed(lessonNameText);
            return "Недопустимое название урока!";
        } else if (lessonNameText.getText().length() > 20){
            makeRed(lessonNameText);
            return "Название урока не должно превышать 20 символов!";
        }
        if (lessonTextText.getText() == null || isDouble(lessonTextText.getText()) == true
                || isInteger(lessonTextText.getText()) == true || lessonTextText.getText().length() == 0){
            makeRed(lessonTextText);
            return "Недопустимый текст урока!";
        } else if (lessonNameText.getText().length() > 500){
            makeRed(lessonTextText);
            return "Текст урока не должен превышать 500 символов!";
        }
        if ((lessonQuestionType.getValue() == null || lessonQuestionType.getValue().length() == 0) && lesson.getQuestionType() == null){
            makeRed(lessonQuestionType);
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
