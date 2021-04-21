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

    public User formUser(String response, String role) throws IOException {
        String login = stringToMap.createMap(response).get("login");
        String password = stringToMap.createMap(response).get("password");
        String firstName = stringToMap.createMap(response).get("firstName");
        String lastName = stringToMap.createMap(response).get("lastName");
        String status = stringToMap.createMap(response).get("status");
        String token = stringToMap.createMap(response).get("token");
        return new User(login, password, firstName, lastName, role, status);
    }

    @FXML
    public void signInButton() throws IOException {
        try {
            message.setText("");
            long start = System.currentTimeMillis();
            String response = restAPI.auth(this);

            System.out.println("Авторизация за " + (System.currentTimeMillis() - start));
            //System.out.println(response);
            if (response == null) {
                System.out.println("Неверный логин или пароль " + (System.currentTimeMillis() - start));

                message.setTextFill(Color.web("red"));
                message.setText("Неверный логин или пароль!");
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(e -> message.setText(""));
                pause.play();

            } else {
                main.hideOverview(anchorPane);
                System.out.println("Закрыли форму " + (System.currentTimeMillis() - start));
                String role = stringToMap.createMap(response).get("role");
                System.out.println("Получили роль " + (System.currentTimeMillis() - start));
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
