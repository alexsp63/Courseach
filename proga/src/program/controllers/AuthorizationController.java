package program.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import program.Main;
import program.models.User;
import program.utils.RestAPI;

public class AuthorizationController {

    @FXML
    private TextField loginText;

    @FXML
    private PasswordField passwordText;

    private User user;
    private boolean signInIsClicked = false;
    private Main main;
    private RestAPI restAPI;


    @FXML
    private void initialize() {
        showDefaultText();
    }

    public void showDefaultText() {
        loginText.setPromptText("Логин");
        passwordText.setPromptText("Пароль");
    }

    public void createAlert(String titleText, String headerText, String contextText){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(main.getPrimaryStage());
        alert.setTitle(titleText);
        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        alert.showAndWait();
    }

    public boolean isSignInIsClicked() {
        return signInIsClicked;
    }

    @FXML
    public void signInButton(ActionEvent actionEvent) {
        String login = loginText.getText();
        String password = passwordText.getText();
        if (login.equals("") || password.equals("")){

            createAlert("Ошибка ввода!",
                    "Для авторизации необходимо заполнить все поля!",
                    "Пожалуйста, заполните поле ввода логина и пароля и попробуйте снова!");

        } else if (restAPI.auth(login, password) == null){

            createAlert("Всё хорошо!",
                    "Логин и пароль верны!",
                    "Добро пожаловать, " + login + "!");
        } else {

            createAlert("Неверный логин или пароль!",
                    "Попробуйте снова!",
                    "Пожалуйста, проверьте ввод и попробуйте снова!");
        }
    }

    @FXML
    public void signUpButton(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(main.getPrimaryStage());
        alert.setTitle("SignUp button is clicked");
        alert.setHeaderText("It's ok");
        alert.setContentText("I wanna sleep");

        alert.showAndWait();
    }

    public void setMain(Main main, RestAPI restAPI) {
        this.main = main;
        this.restAPI = restAPI;
    }
}
