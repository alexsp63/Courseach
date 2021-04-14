package program.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import program.Main;
import program.models.Lesson;
import program.models.Question;
import program.utils.RestAPI;


public class LessonQuestionsController {

    @FXML
    private TableView<Question> questionTable4;

    @FXML
    private TableColumn<Question, String> questionTable4Column;

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

        questionTable4.setItems(main.getQuestions4Data());
    }

    @FXML
    private void initialize(){

        questionTable4Column.setCellValueFactory(cellData -> cellData.getValue().textProperty());
    }
}
