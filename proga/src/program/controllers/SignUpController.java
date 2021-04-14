package program.controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
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
    }

    public boolean isCloseISClicked() {
        return closeISClicked;
    }

    @FXML
    private void cancelIsClicked(){
        main.hideOverview(anchorPane);
        main.showAuthorizationForm();
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
        if (firstNameText.getText() == null || isDouble(firstNameText.getText()) == true
                || isInteger(firstNameText.getText()) == true || firstNameText.getText().length() == 0
                || firstNameText.getText().equals("")){
            return "Недопустимый формат имени!";
        }
        if (lastNameText.getText() == null || isDouble(lastNameText.getText()) == true
                || isInteger(lastNameText.getText()) == true || lastNameText.getText().length() == 0
                || lastNameText.getText().equals("")){
            return "Недопустимый формат фамилии!";
        }
        if (loginText.getText() == null || loginText.getText().equals("")) {
            return "Поле логина не может быть пустым!";
        }
        if (passwordText.getText() == null || passwordText.getText().equals("")) {
            return "Поле пароля не может быть пустым!";
        }
        if (repeatPasswordText.getText() == null || repeatPasswordText.getText().equals("")){
            return "Подтвердите пароль повторным вводом!";
        }
        if (!passwordText.getText().equals(repeatPasswordText.getText())){
            return "Пароли не совпадают!";
        }
        if (unavailableLogins.contains(loginText.getText())) {
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
    private void signUpClicked() {
        if (InputCheck()) {
            User newUser = new User();
            newUser.setFirstName(firstNameText.getText());
            newUser.setLastName(lastNameText.getText());
            newUser.setLogin(loginText.getText());
            newUser.setPassword(passwordText.getText());
            restAPI.postUser(newUser);

            closeISClicked = true;
            main.hideOverview(anchorPane);
            main.showAuthorizationForm();
        }
    }
}
