package program.controllers;

import javafx.stage.Stage;
import program.Main;
import program.models.User;
import program.utils.RestAPI;

public class StatAdminController {

    private User user;
    private Stage stage;
    private Main main;
    private RestAPI restAPI;
    private String token;
    private String type;

    public void setMain(Stage stage, Main main, RestAPI restAPI, String token, User user, String type){
        this.stage = stage;
        this.main = main;
        this.restAPI = restAPI;
        this.token = token;
        this.user = user;
        this.type = type;
    }



}
