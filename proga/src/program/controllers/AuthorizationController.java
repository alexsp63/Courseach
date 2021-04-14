package program.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import program.Main;
import program.models.JSONSerialize;
import program.models.User;
import program.utils.RestAPI;
import program.utils.StringToMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AuthorizationController implements JSONSerialize {

    @FXML
    private TextField loginText;

    @FXML
    private PasswordField passwordText;

    @FXML
    private Label message;

    private boolean signInIsClicked = false;
    private Main main;
    private RestAPI restAPI;
    private AnchorPane anchorPane;
    private StringToMap stringToMap;


    @FXML
    private void initialize() {
        message.setText("");
        showDefaultText();
    }

    public void showDefaultText() {
        loginText.setPromptText("Логин");
        passwordText.setPromptText("Пароль");
    }

    public boolean isSignInIsClicked() {
        return signInIsClicked;
    }

    @FXML
    public void signInButton() throws IOException {
        message.setText("");
        String response = restAPI.auth(this);
        //System.out.println(response);
        if (response == null){

            message.setTextFill(Color.web("red"));
            message.setText("Неверный логин или пароль!");
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(e -> message.setText(""));
            pause.play();

        } else {
            String login = stringToMap.createMap(response).get("login");
            String token = stringToMap.createMap(response).get("token");
            User currentUser = restAPI.getOneUser(login, token);
            String role = currentUser.getRole();
            if (role.equals("ADMIN")){
                main.hideOverview(anchorPane);
                main.showAdminForm(currentUser, token);
            } else if (role.equals("USER")){
                System.out.println(role);
            }
        }
    }

    @FXML
    public void signUpButton(ActionEvent actionEvent) {
        main.hideOverview(anchorPane);
        main.showSignUpForm();
    }

    public void setMain(Main main, RestAPI restAPI, AnchorPane anchorPane, StringToMap stringToMap) {
        this.main = main;
        this.restAPI = restAPI;
        this.anchorPane = anchorPane;
        this.stringToMap = stringToMap;
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    @Override
    public String toJson() {
        Map<String, String> map = new HashMap<>();
        map.put("login", loginText.getText());
        map.put("password", passwordText.getText());
        Gson gson = new Gson();
        return gson.toJson(map);
    }
}
