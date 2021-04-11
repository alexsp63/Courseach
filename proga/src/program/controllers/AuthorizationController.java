package program.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
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

    private boolean signInIsClicked = false;
    private Main main;
    private RestAPI restAPI;
    private AnchorPane anchorPane;
    private StringToMap stringToMap;


    @FXML
    private void initialize() {
        showDefaultText();
    }

    public void showDefaultText() {
        loginText.setPromptText("Логин");
        passwordText.setPromptText("Пароль");
    }

    public void createAlert(String titleText, String headerText, String contextText){
        Alert alert = new Alert(Alert.AlertType.ERROR);
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
    public void signInButton() throws IOException {
        String response = restAPI.auth(this);

        if (response == null){

            createAlert("Ошибка!",
                    "Данные введены неверно!",
                    "Пожалуйста, попробуйте снова!");

        } else {

            String login = stringToMap.createMap(response).get("login");
            String token = stringToMap.createMap(response).get("token");
            User currentUser = restAPI.getOneUser(login, token);
            System.out.println(currentUser.getRole());
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
