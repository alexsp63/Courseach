package program.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import program.Main;
import program.models.Admin;
import program.models.User;

public class AuthorizationController {

    @FXML
    private TextField loginText;

    @FXML
    private PasswordField passwordText;

    private Stage dialogueStage;
    private User user;
    private Admin admin;
    private boolean signInIsClicked = false;
    private Main main;

    @FXML
    private void initialize() {
        showDefaultText();
    }

    public void showDefaultText() {
        loginText.setPromptText("Логин");
        passwordText.setPromptText("Пароль");
    }

    @FXML
    public void signInButton(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(main.getPrimaryStage());
        alert.setTitle("SignIn button is clicked");
        alert.setHeaderText("It's ok");
        alert.setContentText("I wanna sleep");

        alert.showAndWait();
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

    public void setMain(Main main) {
        this.main = main;
    }
}
