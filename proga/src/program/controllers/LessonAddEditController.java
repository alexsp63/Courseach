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

/**
 * Этот контроллер отвечает за форму редактирования уроков - lessonAddEditForm.fxml
 */
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

    /**
     * Установка окна, открывающегося в новой вкладке
     * @param lessonStage - stage
     * @param main - main
     * @param restAPI - restAPI
     * @param token - токен пользователя в системе
     */
    public void setStage(Stage lessonStage, Main main, RestAPI restAPI, String token){
        this.lessonStage = lessonStage;
        this.main = main;
        this.restAPI = restAPI;
        this.token = token;
    }

    /**
     * Окрашивание невалидного текстового поля в красный цвет на 2 секнуды (и затем возвращение стиля назад)
     * @param textField - окрашиваемое поле
     */
    public static void makeRed(TextField textField){
        textField.setStyle("-fx-border-color: red;");
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> textField.setStyle("-fx-border-color: #84cdff;"));
        pause.play();
    }

    /**
     * Окрашивание невалидной текстовой области в красный цвет на 2 секнуды (и затем возвращение стиля назад)
     * @param textArea - окрашиваемая текстовая область
     */
    public static void makeRed(TextArea textArea){
        textArea.setStyle("-fx-border-color: red;");
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> textArea.setStyle("-fx-border-color: #84cdff;"));
        pause.play();
    }

    /**
     * Окрашивание пустого ComboBox'а в красный цвет на 2 секнуды (и затем возвращение стиля назад)
     * @param comboBox - окрашиваемаый ComboBox
     */
    public void makeRed(ComboBox<String> comboBox){
        comboBox.setStyle("-fx-border-color: red;");
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> comboBox.setStyle("-fx-border-color: #bfd5ff;"));
        pause.play();
    }

    /**
     * Инициализация TextField, TextArea, ComboBox и задание им стиля и поведения
     */
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

    /**
     * Проверка на нажатие кнопки сохранения
     * @return true, если нажата и валидация прошла успешно, иначе - false
     */
    public boolean isSaveIsClicked() {
        return saveIsClicked;
    }

    /**
     * Установка урока для изменения
     * @param lesson - урок для изменения
     */
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

    /**
     * Формирование сообщения об ошибке
     * @return сообщение об ошибке
     */
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

    /**
     * Проверка ввода
     * @return true при корректном вводе, иначе - false и установка сообщения об ошибке
     */
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

    /**
     * Нажата кнопка записать:
     * Происходит валидация ввода, и если всё хорошо, окно закрывается
     */
    @FXML
    public void write(){
        if (inputCheck()){
            lesson.setName(lessonNameText.getText());
            lesson.setTextText(lessonTextText.getText());
            if (lesson.getQuestionType() == null && (restAPI.getQuestionsByLesson(token, lesson).size() == 0)) {
                lesson.setQuestionType(lessonQuestionType.getValue());
            }
            if (lessonQuestionType.getValue() != null){
                lesson.setQuestionType(lessonQuestionType.getValue());
            }
            //System.out.println(lesson.getQuestionType() + " " + lessonQuestionType.getValue());
            saveIsClicked = true;
            lessonStage.close();
        }
    }

    /*
    Многие думают, что британцы серьёзные ребята, постоянно пьющие чай (они называют чашечку их любимого напитка ласковым словом - cuppa) и не умеющие веселиться. Что ж, про чай может и правда, но уж что, но повеселиться после тяжёлой недели они любят.
     */
    /**
     * Администратор передумал создавать/редактировать вопрос
     */
    @FXML
    public void cancel(){
        lessonStage.close();
    }
}
