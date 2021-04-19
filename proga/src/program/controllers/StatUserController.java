package program.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import program.Main;
import program.models.AnswerHistory;
import program.models.Lesson;
import program.models.Statistics;
import program.models.User;
import program.utils.DateUtil;
import program.utils.RestAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StatUserController {

    @FXML
    private Label question1Text;

    @FXML
    private Label question2Text;

    @FXML
    private Label question3Text;

    @FXML
    private Label question4Text;

    @FXML
    private Label question5Text;

    @FXML
    private Label question6Text;

    @FXML
    private Label question7Text;

    @FXML
    private Label question8Text;

    @FXML
    private Label question9Text;

    @FXML
    private Label question10Text;

    @FXML
    private Label question1Answer;

    @FXML
    private Label question2Answer;

    @FXML
    private Label question3Answer;

    @FXML
    private Label question4Answer;

    @FXML
    private Label question5Answer;

    @FXML
    private Label question6Answer;

    @FXML
    private Label question7Answer;

    @FXML
    private Label question8Answer;

    @FXML
    private Label question9Answer;

    @FXML
    private Label question10Answer;

    @FXML
    private TableView<Statistics> statTable;

    @FXML
    private TableColumn<Statistics, String> statDate;

    @FXML
    private TableColumn<Statistics, String> statScore;

    private Main main;
    private Stage stage;
    private RestAPI restAPI;
    private String token;
    private Lesson lesson;
    private User user;

    public void setMain(Stage stage, Main main, RestAPI restAPI, String token, Lesson lesson, User user) {
        this.stage = stage;
        this.main = main;
        this.restAPI = restAPI;
        this.token = token;
        this.lesson = lesson;
        this.user = user;

        statTable.setItems(main.getStatisticsData());

        main.updateStatisticsData(token, lesson, user);
    }

    @FXML
    private void initialize() {

        statDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        statScore.setCellValueFactory(cellData -> cellData.getValue().scoreProperty());

        statTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, olValue, newValue) -> showStatDetails(newValue)
        );

        showStatDetails(null);
    }

    public void showStatDetails(Statistics statistics) {

        Label[] textLabels = {question1Text, question2Text, question3Text, question4Text, question5Text,
                question6Text, question7Text, question8Text, question9Text, question10Text};

        List<Label> textLabels1 = Arrays.stream(textLabels).collect(Collectors.toList());

        Label[] answerLabels = {
                question1Answer, question2Answer, question3Answer, question4Answer,
                question5Answer, question6Answer, question7Answer, question8Answer,
                question9Answer, question10Answer};

        List<Label> answerLabels1 = Arrays.stream(answerLabels).collect(Collectors.toList());

        if (statistics != null) {
            List<AnswerHistory> answerHistoryList = restAPI.getAnswerHistoryByStatistics(token, statistics);

            for (int i = 0; i < 10; i++) {
                textLabels1.get(i).setText(answerHistoryList.get(i).getQuestion().getText());
                if (answerHistoryList.get(i).getIsCorrect() == true){
                    answerLabels1.get(i).setText("Верный");
                } else {
                    answerLabels1.get(i).setText("Неверный");
                }
            }
        } else {

            for (Label label : textLabels) {
                label.setText("");
            }
            for (Label label : answerLabels) {
                label.setText("");
            }
        }
    }
}
