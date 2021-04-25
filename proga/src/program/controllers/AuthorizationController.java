package program.controllers;

import com.google.gson.Gson;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthorizationController implements JSONSerialize {

    @FXML
    private TextField loginText;

    @FXML
    private PasswordField passwordText;

    @FXML
    private Label message;

    @FXML
    private Group authGroup;

    private boolean signInIsClicked = false;
    private Main main;
    private RestAPI restAPI;
    private AnchorPane anchorPane;


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

    private void createSleeper(User currentUser, String mayBeToken){
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
                if (currentUser.getRole().equals("ADMIN")) {
                    main.showAdminForm(currentUser, mayBeToken);
                } else if (currentUser.getRole().equals("USER")){
                    main.showUserForm(currentUser, mayBeToken);
                }
            }
        });
        new Thread(sleeper).start();
    }

    @FXML
    public void signInButton() throws IOException {
        try {
            message.setText("");

            List<String> respKey = new ArrayList<>();
            Map<String, User> response = restAPI.auth(this);
            for (String key: response.keySet()){
                respKey.add(key);
            }
            String mayBeToken = respKey.get(0);
            User currentUser = response.get(mayBeToken);

            if (currentUser.getLogin() == null) {

                //то это сообщение об ошибке, а не токен

                message.setTextFill(Color.web("red"));
                message.setText(mayBeToken);
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(e -> message.setText(""));
                pause.play();

            } else {
                createSleeper(currentUser, mayBeToken);
            }
        } catch (IndexOutOfBoundsException e){}
        catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(main.getPrimaryStage());
            alert.setTitle("Отсутствует подключение!");
            alert.setHeaderText("Сервер не запущен!");
            alert.setContentText("Пожалуйста, запустите зервер и попробуйте снова!");
            alert.showAndWait();
        }
    }

    @FXML
    public void signUpButton(ActionEvent actionEvent) {
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
                main.showSignUpForm();
            }
        });
        new Thread(sleeper).start();
    }

    public void setMain(Main main, RestAPI restAPI, AnchorPane anchorPane) {
        this.main = main;
        this.restAPI = restAPI;
        this.anchorPane = anchorPane;

        main.createAppearEffect(anchorPane, 0.5);
    }

    @FXML
    public void showAuthInfo(){
        main.showInfo("author");
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
