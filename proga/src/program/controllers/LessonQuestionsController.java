package program.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.json.JSONException;
import program.Main;
import program.models.Lesson;
import program.models.Question;
import program.utils.RestAPI;

import java.util.ArrayList;
import java.util.List;


public class LessonQuestionsController {

    @FXML
    private TableView<Question> questionTable;

    @FXML
    private TableColumn<Question, String> questionTableColumn;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label lastLabel;

    @FXML
    private Label questionText;

    @FXML
    private Label questionDescription;

    @FXML
    private Label correctAnswer;

    @FXML
    private Label incorrect1;

    @FXML
    private Label incorrect2;

    @FXML
    private Label incorrect3;

    @FXML
    private Label incorrect1Label;

    @FXML
    private Label incorrect2Label;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Label message;

    @FXML
    private Label textLabel;

    @FXML
    private Label corrLabel;

    private Lesson lesson;
    private RestAPI restAPI;
    private String token;
    private Main main;
    private Stage stage;

    public void setStage(Stage stage, Main main, RestAPI restAPI, Lesson lesson, String token){
        this.stage = stage;
        this.main = main;
        this.restAPI = restAPI;
        this.lesson = lesson;
        this.token = token;

        questionTable.setItems(main.getQuestionsData());
        descriptionLabel.setWrapText(true);
        lastLabel.setWrapText(true);

        if (restAPI.getQuestionsByLesson(token, lesson).size() == 10){
            addButton.setDisable(true);
        }

        editButton.setDisable(true);
        deleteButton.setDisable(true);

    }

    @FXML
    private void initialize(){

        questionTableColumn.setCellValueFactory(cellData -> cellData.getValue().textProperty());
        showQuestionDetails(null);

        questionTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, olValue, newValue) -> showQuestionDetails(newValue)
        );
    }

    public void showQuestionDetails(Question question){

        if (question != null){
            editButton.setDisable(false);
            deleteButton.setDisable(false);
            message.setText("Информация о вопросе");
            textLabel.setText("Текст вопроса: ");
            questionText.setText(question.getText());
            descriptionLabel.setText("Описание (небольшое правило, которое будет показываться ученику при ответе на вопрос, обоснование ответа):");
            questionDescription.setText(question.getDescription());
            corrLabel.setText("Правильный ответ: ");
            correctAnswer.setText(question.getCorrectAnswer());
            if (question.getType().equals("2 варианта ответа")){
                incorrect1Label.setText("Неправильный ответ:");
                incorrect1.setText(question.getIncorrect1());
            } else if (question.getType().equals("4 варианта ответа")){
                incorrect1Label.setText("Неправильный ответ:");
                incorrect1.setText(question.getIncorrect1());
                incorrect2Label.setText("Ещё один неправильный ответ:");
                incorrect2.setText(question.getIncorrect2());
                incorrect2Label.setText("И ещё один неправильный ответ:");
                incorrect3.setText(question.getIncorrect3());
            }
        } else {
            message.setText("Выберите вопрос для отображения информации по нему");
            textLabel.setText("");
            questionText.setText("");
            descriptionLabel.setText("");
            questionDescription.setText("");
            corrLabel.setText("");
            correctAnswer.setText("");
            incorrect1Label.setText("");
            incorrect1.setText("");
            incorrect2Label.setText("");
            incorrect2.setText("");
            lastLabel.setText("");
            incorrect3.setText("");
        }
    }

    @FXML
    private void addQuestion() throws JSONException {
        Question newQuestion = new Question();
        boolean okIsClicked = main.showQuestionAddEditForm(newQuestion, token, lesson);
        if (okIsClicked){
            restAPI.postQuestion(newQuestion, token);
            main.updateQuestionData(token, lesson);
            if (restAPI.getQuestionsByLesson(token, lesson).size() >= 10){
                addButton.setDisable(true);
            }
        }
    }

    @FXML
    private void editQuestion() throws JsonProcessingException, JSONException {
        Question selectedItem = questionTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null){
            boolean okIsClicked = main.showQuestionAddEditForm(selectedItem, token, lesson);
            if (okIsClicked){
                restAPI.putQuestion(selectedItem, token);
                main.updateQuestionData(token, lesson);
            }
        }
    }

    @FXML
    private void deleteQuestion(){
        Question selectedItem = questionTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null){
            if (restAPI.deleteQuestion(selectedItem, token)){
                questionTable.getItems().remove(selectedItem);
                if (restAPI.getQuestionsByLesson(token, lesson).size() < 10){
                    addButton.setDisable(false);
                }
            }
        }
    }
}
