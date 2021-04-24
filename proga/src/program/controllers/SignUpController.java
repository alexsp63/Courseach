package program.controllers;

import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.animation.PauseTransition;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import program.Main;
import program.models.User;
import program.utils.RestAPI;

import java.util.List;

public class SignUpController {

    @FXML
    private TextField firstNameText;

    @FXML
    private TextField lastNameText;

    @FXML
    private TextField loginText;

    @FXML
    private PasswordField passwordText;

    @FXML
    private PasswordField repeatPasswordText;

    @FXML
    private Label errorMes;

    private User user;
    private Main main;
    private RestAPI restAPI;
    private AnchorPane anchorPane;
    private boolean closeISClicked;

    public void setMain(Main main, RestAPI restAPI, AnchorPane anchorPane) {
        this.main = main;
        this.restAPI = restAPI;
        this.anchorPane = anchorPane;

        main.createAppearEffect(anchorPane, 0.5);
    }

    @FXML
    private void initialize(){
        TextField[] textFields = {firstNameText, lastNameText, loginText, passwordText, repeatPasswordText};
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
    }

    public static void makeRed(TextField textField){
        textField.setStyle("-fx-border-color: #ff0000; -fx-border-width: 0 0 2 0;");
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> textField.setStyle("-fx-border-color: #84cdff; -fx-border-width: 0 0 2 0;"));
        pause.play();
    }

    public boolean isCloseISClicked() {
        return closeISClicked;
    }

    @FXML
    private void cancelIsClicked(){
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

    public static boolean isDouble(String str) {
        //Проверка на не целое число (ну имя должно иметь хотя бы одну букву, как и фамилия)
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isInteger(String str) {
        //Проверка на целое число (нам нельзя, чтобы имя или фамилия было целым числом)
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String errorMessage(){
        List<String> unavailableLogins = restAPI.getAllLogins();
        if (firstNameText.getText().trim() == null || isDouble(firstNameText.getText()) == true
                || isInteger(firstNameText.getText()) == true || firstNameText.getText().length() == 0
                || firstNameText.getText().trim().equals("")){
            makeRed(firstNameText);
            return "Недопустимый формат имени!";
        }
        if (lastNameText.getText() == null || isDouble(lastNameText.getText()) == true
                || isInteger(lastNameText.getText()) == true || lastNameText.getText().length() == 0
                || lastNameText.getText().trim().equals("")){
            makeRed(lastNameText);
            return "Недопустимый формат фамилии!";
        }
        if (loginText.getText() == null || loginText.getText().trim().equals("")) {
            makeRed(loginText);
            return "Поле логина не может быть пустым!";
        }
        if (passwordText.getText() == null || passwordText.getText().trim().equals("")) {
            makeRed(passwordText);
            return "Поле пароля не может быть пустым!";
        }
        if (repeatPasswordText.getText() == null || repeatPasswordText.getText().trim().equals("")){
            makeRed(repeatPasswordText);
            return "Подтвердите пароль повторным вводом!";
        }
        if (!passwordText.getText().equals(repeatPasswordText.getText())){
            makeRed(repeatPasswordText);
            return "Пароли не совпадают!";
        }
        if (unavailableLogins.contains(loginText.getText())) {
            makeRed(loginText);
            return "Введённый логин уже используется другим пользователем!";
        }
        return "";
    }

    private boolean InputCheck(){
        String message = errorMessage();
        if (message.length() == 0) {
            return true;
        } else {
            errorMes.setText(message);
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(e -> errorMes.setText(""));
            pause.play();
            return false;
        }
    }

    @FXML
    private void signUpClicked() throws UnirestException {
        if (InputCheck()) {
            User newUser = new User();
            newUser.setFirstName(firstNameText.getText());
            newUser.setLastName(lastNameText.getText());
            newUser.setLogin(loginText.getText());
            newUser.setPassword(passwordText.getText());
            restAPI.postUser(newUser);

            closeISClicked = true;
            cancelIsClicked();
        }
    }
}
