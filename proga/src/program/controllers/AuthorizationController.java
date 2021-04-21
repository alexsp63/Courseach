package program.controllers;

import com.google.gson.Gson;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import java.net.ConnectException;
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
    private void initialize() throws UnirestException {
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

    public User formUser(String response, String role) throws IOException {
        String login = stringToMap.createMap(response).get("login");
        String password = stringToMap.createMap(response).get("password");
        String firstName = stringToMap.createMap(response).get("firstName");
        String lastName = stringToMap.createMap(response).get("lastName");
        String status = stringToMap.createMap(response).get("status");
        return new User(login, password, firstName, lastName, role, status);
    }

    @FXML
    public void signInButton() throws IOException {
        try {
            message.setText("");

            String response = restAPI.auth(this);

            if (response.equals("Неверные учетные данные пользователя")
            || response.equals("Учетная запись пользователя заблокирована")) {

                message.setTextFill(Color.web("red"));
                message.setText(response);
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(e -> message.setText(""));
                pause.play();

            } else {
                main.hideOverview(anchorPane);
                String role = stringToMap.createMap(response).get("role");
                if (role.equals("ADMIN")) {
                    User currentUser = formUser(response, role);
                    String token = stringToMap.createMap(response).get("token");
                    main.showAdminForm(currentUser, token);
                } else if (role.equals("USER")) {
                    User currentUser = formUser(response, role);
                    String token = stringToMap.createMap(response).get("token");
                    main.showUserForm(currentUser, token);
                }
            }
        } catch (ConnectException | IndexOutOfBoundsException e){}
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
