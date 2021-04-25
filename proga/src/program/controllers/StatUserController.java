package program.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import program.Main;
import program.models.AnswerHistory;
import program.models.Lesson;
import program.models.Statistics;
import program.models.User;
import program.utils.RestAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер, отвечающий за форму отображения статистики для пользователя
 */
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

    @FXML
    private Label minLabel;

    @FXML
    private Label avgLabel;

    @FXML
    private Label maxLabel;

    private Main main;
    private Stage stage;
    private RestAPI restAPI;
    private String token;
    private Lesson lesson;
    private User user;

    /**
     * Инициализация главных элементов и статистических результатов: мин, макс и среднее
     * @param stage - сцена
     * @param main - main
     * @param restAPI - restAPI
     * @param token - токен
     * @param lesson - урок
     * @param user - пользователь
     */
    public void setMain(Stage stage, Main main, RestAPI restAPI, String token, Lesson lesson, User user) {
        this.stage = stage;
        this.main = main;
        this.restAPI = restAPI;
        this.token = token;
        this.lesson = lesson;
        this.user = user;

        statTable.setItems(main.getStatisticsData());

        main.updateStatisticsData(token, lesson, user);

        List<Statistics> statistics = restAPI.getStatiscticsByLessonAndUser(token, lesson, user);
        List<Integer> scores = new ArrayList<>();
        for (Statistics statistics1: statistics){
            scores.add(statistics1.getScore());
        }
        double sum = 0;
        int min = 10*Collections.min(scores);
        int max = 10*Collections.max(scores);
        for (int el: scores){
            sum += el;
        }
        double avg = 10*(sum/scores.size());
        minLabel.setText("MIN: " + min + "%");
        avgLabel.setText("AVERAGE: " + Math.round(avg) + "%");
        maxLabel.setText("MAX: " + max + "%");
    }

    /**
     * Инициализация
     * Задание поведения таблице статистики
     */
    @FXML
    private void initialize() {

        statDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        statScore.setCellValueFactory(cellData -> cellData.getValue().scoreProperty());

        statTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, olValue, newValue) -> showStatDetails(newValue)
        );

        showStatDetails(null);
    }

    /**
     * Отображение подробностей - истории ответов
     * @param statistics - для выбранного объекта статистики
     */
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
                    answerLabels1.get(i).setText("Верно");
                    answerLabels1.get(i).setTextFill(Color.web("green"));
                } else {
                    answerLabels1.get(i).setText("Неверно");
                    answerLabels1.get(i).setTextFill(Color.web("red"));
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
