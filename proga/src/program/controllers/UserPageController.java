package program.controllers;

import javafx.animation.PauseTransition;
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

    private Main main;
    private RestAPI restAPI;
    private AnchorPane anchorPane;
    private User user;
    private String token;

    public void setMain(User user, Main main, RestAPI restAPI, AnchorPane anchorPane, String token) {
        this.user = user;
        this.main = main;
        this.restAPI = restAPI;
        this.anchorPane = anchorPane;
        this.token = token;

        lessonTable.setItems(main.getLessonData());
    }

    @FXML
    private void initialize() {

        userLogin.setEditable(false);
        lessonQuestionsButton.setDisable(true);

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

    public void showLessonDetails(Lesson lesson){
        if (lesson != null){
            if (restAPI.getQuestionsByLesson(token, lesson).size() < 10){
                lessonQuestionsButton.setDisable(true);
                lessonTestMessage.setTextFill(Color.web("yellow"));
                lessonTestMessage.setText("Тест по этому уроку сейчас недоступен, так как вопросы находятся в разработке. Вы сможете пройти тест очень скоро, а в данный момент можете внимательно ознакомиться с текстом урока.");
                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.setOnFinished(e -> lessonTestMessage.setText(""));
                pause.play();
            } else {
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
        } else {
            lessonQuestionsButton.setDisable(true);
            lessonName.setText("");
            lessonText.setText("");
            lessonQuestionType.setText("");
        }
    }

    public void setUser(User user){
        userFirstName.setText(user.getFirstName());
        userLastName.setText(user.getLastName());
        userLogin.setText(user.getLogin());
        userNewPassword.setText("");
        userRepeatedPassword.setText("");
    }

    private String userError(){
        if (userFirstName.getText() == null || isDouble(userFirstName.getText()) == true
                || isInteger(userFirstName.getText()) == true || userFirstName.getText().length() == 0
                || userFirstName.getText().equals("")){
            return "Недопустимый формат имени!";
        }
        if (userLastName.getText() == null || isDouble(userLastName.getText()) == true
                || isInteger(userLastName.getText()) == true || userLastName.getText().length() == 0
                || userLastName.getText().equals("")){
            return "Недопустимый формат фамилии!";
        }
        if (userNewPassword.getText() != null || !userNewPassword.getText().equals("")) {
            if (!userRepeatedPassword.getText().equals(userNewPassword.getText())){
                return "Подтвердите новый пароль!";
            }
        }
        return "";
    }

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

    static void setErrorMessage(Label userErrorMessage, Main main, String token) {
        userErrorMessage.setTextFill(Color.web("green"));
        userErrorMessage.setText("Информация обновлена");
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> userErrorMessage.setText(""));
        pause.play();
        main.updateUserTable(token);
    }

    @FXML
    public void userCancel(){
        setUser(user);
    }

    @FXML
    public void startTest(){
        Lesson selectedLesson = lessonTable.getSelectionModel().getSelectedItem();
        if (selectedLesson != null){
            List<Question> questions = restAPI.getQuestionsByLesson(token, selectedLesson);
            main.showTestWindow(token, questions, selectedLesson);
        }
    }

    @FXML
    public void logOut(){
        //TODO: прописать logout в restAPI
        main.hideOverview(anchorPane);
        main.showAuthorizationForm();
    }
}
