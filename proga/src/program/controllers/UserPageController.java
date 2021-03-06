package program.controllers;

import javafx.animation.PauseTransition;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import program.Main;
import program.models.Lesson;
import program.models.Question;
import program.models.User;
import program.utils.RestAPI;

import java.util.List;

import static program.controllers.SignUpController.isDouble;
import static program.controllers.SignUpController.isInteger;

/**
 * Контроллер отвечающий за окно пользователя - userMainPage.fxml
 */
public class UserPageController {

    @FXML
    private TextField userFirstName;

    @FXML
    private TextField userLastName;

    @FXML
    private TextField userLogin;

    @FXML
    private PasswordField userNewPassword;

    @FXML
    private PasswordField userRepeatedPassword;

    @FXML
    private Label userErrorMessage;

    @FXML
    private Label lessonName;

    @FXML
    private Label lessonText;

    @FXML
    private Label lessonQuestionType;

    @FXML
    private Label lessonTestMessage;

    @FXML
    private Button lessonQuestionsButton;

    @FXML
    private TableView<Lesson> lessonTable;

    @FXML
    private TableColumn<Lesson, String> lessonColumnName;

    @FXML
    private Button showStatisticsButton;

    private Main main;
    private RestAPI restAPI;
    private AnchorPane anchorPane;
    private User user;
    private String token;

    /**
     * Инициализация главных элементов формы
     * @param user - авторизованного пользователя
     * @param main - main
     * @param restAPI - restAPI
     * @param anchorPane - окно пользователя
     * @param token - токен
     */
    public void setMain(User user, Main main, RestAPI restAPI, AnchorPane anchorPane, String token) {
        this.user = user;
        this.main = main;
        this.restAPI = restAPI;
        this.anchorPane = anchorPane;
        this.token = token;

        lessonTable.setItems(main.getLessonData());
        main.createAppearEffect(anchorPane, 1.5);
    }

    /**
     * Инициализация и установление поведения элементам взаимодействия с пользователем
     */
    @FXML
    private void initialize() {

        TextField[] textFields = {userFirstName, userLastName, userNewPassword, userRepeatedPassword};
        for (TextField textField: textFields){
            textField.setOnMouseClicked(mouseEvent -> {
                textField.setStyle("-fx-border-color: #14a9ff; -fx-border-width: 0 0 2 0;");
                for (TextField textField1: textFields){
                    if (textField1 != textField){
                        textField1.setStyle("-fx-border-color: #84cdff; -fx-border-width: 0 0 2 0;");
                    }
                }
            });
        }

        userLogin.setEditable(false);
        lessonQuestionsButton.setDisable(true);
        showStatisticsButton.setDisable(true);
        showStatisticsButton.setVisible(false);

        lessonColumnName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        lessonName.setWrapText(true);
        lessonText.setWrapText(true);
        lessonQuestionType.setWrapText(true);
        lessonTestMessage.setWrapText(true);

        showLessonDetails(null);

        lessonTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, olValue, newValue) -> showLessonDetails(newValue)
        );
    }

    /**
     * Отображение данных по выбранному уроку
     * @param lesson - выбранный урок
     */
    public void showLessonDetails(Lesson lesson){
        if (lesson != null){
            if (restAPI.getQuestionsByLesson(token, lesson).size() < 10){
                lessonQuestionsButton.setDisable(true);
                lessonTestMessage.setStyle("-fx-text-fill: #ff5900; -fx-font-size: 14");
                lessonTestMessage.setText("Тест по этому уроку сейчас недоступен, так как вопросы находятся в разработке. Вы сможете пройти тест очень скоро, а в данный момент можете внимательно ознакомиться с текстом урока.");
                PauseTransition pause = new PauseTransition(Duration.seconds(5));
                pause.setOnFinished(e -> lessonTestMessage.setText(""));
                pause.play();
            } else {
                lessonTestMessage.setText("");
                lessonQuestionsButton.setDisable(false);
            }
            lessonName.setText(lesson.getName());
            lessonText.setText(lesson.getTextText());
            if (lesson.getQuestionType().equals("Открытый вопрос")){
                lessonQuestionType.setText("Вопросы в тесте этого урока будут с открытым ответом, вам необходимо будет напечатать Ваш ответ в поле ответа.");
            }
            if (lesson.getQuestionType().equals("2 варианта ответа")){
                lessonQuestionType.setText("Вопросы в тесте этого урока содержат 2 варианта ответа: один из них правильный, один нет. Ваша задача - выбрать верный.");
            }
            if (lesson.getQuestionType().equals("4 варианта ответа")){
                lessonQuestionType.setText("Вопросы в тесте этого урока содержат 4 варианта ответа, и только один из них правильный. Ваша задача - выбрать верный.");
            }
            if (restAPI.getStatiscticsByLessonAndUser(token, lesson, user).size() > 0){
                lessonQuestionsButton.setText("Пройти тест снова");
                showStatisticsButton.setDisable(false);
                showStatisticsButton.setVisible(true);
            } else {
                lessonQuestionsButton.setText("Пройти тест");
                showStatisticsButton.setDisable(true);
                showStatisticsButton.setVisible(false);
            }
        } else {
            lessonQuestionsButton.setDisable(true);
            lessonName.setText("");
            lessonText.setText("");
            lessonQuestionType.setText("");
        }
    }

    /**
     * Установка пользователю в личном кабинете его данных
     * @param user - авторизованный пользователь
     */
    public void setUser(User user){
        userFirstName.setText(user.getFirstName());
        userLastName.setText(user.getLastName());
        userLogin.setText(user.getLogin());
        userNewPassword.setText("");
        userRepeatedPassword.setText("");
    }

    /**
     * Формирование сообщения об ошибке
     * @return сообщение об ошибке
     */
    private String userError(){
        if (userFirstName.getText() == null || isDouble(userFirstName.getText()) == true
                || isInteger(userFirstName.getText()) == true || userFirstName.getText().length() == 0
                || userFirstName.getText().equals("")){
            SignUpController.makeRed(userFirstName);
            return "Недопустимый формат имени!";
        }
        if (userLastName.getText() == null || isDouble(userLastName.getText()) == true
                || isInteger(userLastName.getText()) == true || userLastName.getText().length() == 0
                || userLastName.getText().equals("")){
            SignUpController.makeRed(userLastName);
            return "Недопустимый формат фамилии!";
        }
        if (userNewPassword.getText() != null || !userNewPassword.getText().equals("")) {
            if (!userRepeatedPassword.getText().equals(userNewPassword.getText())){
                SignUpController.makeRed(userNewPassword);
                SignUpController.makeRed(userRepeatedPassword);
                return "Подтвердите новый пароль!";
            }
        }
        return "";
    }

    /**
     * Проверка пользовательского ввода
     * @return состояние ввода
     */
    private boolean userInputCheck(){
        String message = userError();
        if (message.length() == 0) {
            userErrorMessage.setText("");
            return true;
        } else {
            userErrorMessage.setTextFill(Color.web("red"));
            userErrorMessage.setText(message);
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(e -> userErrorMessage.setText(""));
            pause.play();
            return false;
        }
    }

    /**
     * Сохранение изменений о пользователе
     */
    @FXML
    public void userSave(){
        if (userInputCheck()){
            userErrorMessage.setText("");
            User oldUser = new User(user.getLogin(),
                    user.getPassword(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getRole(),
                    user.getStatus());
            user.setFirstName(userFirstName.getText());
            user.setLastName(userLastName.getText());
            if (!userNewPassword.equals("") && userNewPassword.getText() != null && userNewPassword.getText().length() != 0){
                user.setPassword(userNewPassword.getText());
            }
            if (oldUser.getFirstName() != user.getFirstName() ||
                    oldUser.getLastName() != user.getLastName() ||
                    oldUser.getPassword() != user.getPassword()){
                restAPI.putUser(user, token);
                setUser(user);
                setErrorMessage(userErrorMessage, main, token);
            }
        }
    }

    /**
     * Установка сообщения об ошибке
     * @param userErrorMessage - сообщение об ошибке
     * @param main - main
     * @param token - токен
     */
    static void setErrorMessage(Label userErrorMessage, Main main, String token) {
        userErrorMessage.setTextFill(Color.web("green"));
        userErrorMessage.setText("Информация обновлена");
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> userErrorMessage.setText(""));
        pause.play();
        main.updateUserTable(token);
    }

    /**
     * Пользователь решил отменить введённые, но не сохранёные изменения
     */
    @FXML
    public void userCancel(){
        setUser(user);
    }

    /**
     * Пользователь начал тестирование
     */
    @FXML
    public void startTest(){
        Lesson selectedLesson = lessonTable.getSelectionModel().getSelectedItem();
        if (selectedLesson != null){
            List<Question> questions = restAPI.getQuestionsByLesson(token, selectedLesson);
            main.showTestWindow(token, questions, selectedLesson, user);
        }
    }

    /**
     * Пользователь захотел посмотреть свою статистику по тому или инаму уроку
     */
    @FXML
    public void showStat(){
        Lesson selectedLesson = lessonTable.getSelectionModel().getSelectedItem();
        main.showStatUserForm(token, selectedLesson, this.user);
    }

    /**
     * Выход из личного кабинета и возвращение к окну авторизации
     */
    @FXML
    public void logOut(){
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    main.hideOverview(anchorPane, 0.5);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                main.showAuthorizationForm();
            }
        });
        new Thread(sleeper).start();
    }

    /**
     * Показ пользователю информации о программе
     */
    @FXML
    public void showInfo(){
        main.showInfo("user");
    }
}
