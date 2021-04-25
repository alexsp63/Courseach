package program.controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import program.Main;
import program.models.Lesson;
import program.models.Question;
import program.utils.RestAPI;

/**
 * Контроллер, отвечающий за добавление/изменение вопросов - questionsAddEditForm.fxml
 */
public class QuestionsAddEditController {

    @FXML
    private TextArea questionText;

    @FXML
    private TextArea questionDescription;

    @FXML
    private TextArea questionCorrect;

    @FXML
    private TextArea questionIncorrect1;

    @FXML
    private TextArea questionIncorrect2;

    @FXML
    private TextArea questionIncorrect3;

    @FXML
    private Label message;

    private Stage questionStage;
    private Main main;
    private RestAPI restAPI;
    private String token;
    private Question question;
    private Lesson lesson;
    private boolean saveIsClicked;

    /**
     * Инициализация главных элементов
     * @param questionStage - окна
     * @param main - main
     * @param restAPI - restAPI
     * @param token - токен
     * @param lesson - урок, которому принадлежат вопросы
     */
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

    /**
     * Установка информации о вопросе сбоку от таблицы
     * @param question - вопрос
     */
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

    /**
     * Нажата ли кнопка сохранения
     * @return состояние нажатия кнопки
     */
    public boolean isSaveIsClicked() {
        return saveIsClicked;
    }

    /**
     * Инициализоция
     */
    @FXML
    private void initialize(){}
    /**
     * Формирование сообщения об ошибке при некорректном вооде
     * @return сообщение об ошибке
     */
    public String errorMessage(){
        if (questionText.getText() == null || questionText.getText().length() == 0 || questionText.getText().equals("")){
            LessonAddEditController.makeRed(questionText);
            return "Введите текст вопроса!";
        } else if (questionText.getText().length() > 300) {
            LessonAddEditController.makeRed(questionText);
            return "Текст вопроса не должен превышать 300 символов!";
        }
        else if (questionDescription.getText() == null || questionDescription.getText().length() == 0 || questionDescription.getText().equals("")){
            LessonAddEditController.makeRed(questionDescription);
            return "Введите пояснение к ответу!";
        } else if (questionDescription.getText().length() > 50){
            LessonAddEditController.makeRed(questionDescription);
            return "Пояснение не должно превышать 50 символов!";
        }
        else if (questionCorrect.getText() == null || questionCorrect.getText().length() == 0 || questionCorrect.getText().equals("")){
            LessonAddEditController.makeRed(questionCorrect);
            return "Введите правильный ответ!";
        } else if (questionCorrect.getText().length() > 20){
            LessonAddEditController.makeRed(questionCorrect);
            return "Правильный ответ не должен превышать 20 символов!";
        }
        if (lesson.getQuestionType().equals("2 варианта ответа")) {
            if (questionIncorrect1.getText() == null || questionIncorrect1.getText().length() == 0 || questionIncorrect1.getText().equals("")) {
                LessonAddEditController.makeRed(questionIncorrect1);
                return "Введите неправильный вариант ответа!";
            } else if (questionIncorrect1.getText().length() > 20){
                LessonAddEditController.makeRed(questionIncorrect1);
                return "Неправильный ответ не должен превышать 20 символов!";
            }
        }
        if (lesson.getQuestionType().equals("4 варианта ответа")) {
            if (questionIncorrect1.getText() == null || questionIncorrect1.getText().length() == 0 || questionIncorrect1.getText().equals("")) {
                LessonAddEditController.makeRed(questionIncorrect1);
                return "Введите первый неправильный вариант ответа!";
            }  else if (questionIncorrect1.getText().length() > 20){
                LessonAddEditController.makeRed(questionIncorrect1);
                return "Неправильный ответ не должен превышать 20 символов!";
            }
            else if (questionIncorrect2.getText() == null || questionIncorrect2.getText().length() == 0 || questionIncorrect2.getText().equals("")) {
                LessonAddEditController.makeRed(questionIncorrect2);
                return "Введите второй неправильный вариант ответа!";
            }  else if (questionIncorrect2.getText().length() > 20){
                LessonAddEditController.makeRed(questionIncorrect2);
                return "Неправильный ответ не должен превышать 20 символов!";
            }
            else if (questionIncorrect3.getText() == null || questionIncorrect3.getText().length() == 0 || questionIncorrect3.getText().equals("")) {
                LessonAddEditController.makeRed(questionIncorrect3);
                return "Введите третий неправильный вариант ответа!";
            }  else if (questionIncorrect3.getText().length() > 20){
                LessonAddEditController.makeRed(questionIncorrect3);
                return "Неправильный ответ не должен превышать 20 символов!";
            }
        }
        return "";
    }

    /**
     * Проверка ввода
     * @return валидность ввода
     */
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

    /**
     * Нажата кнопка сохранения вопроса
     */
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

    /**
     * Администратор передумал добавлять/изменять вопрос и нажал кнопку отмены
     */
    @FXML
    public void cancelQ(){
        questionStage.close();
    }
}
