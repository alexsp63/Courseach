package program.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.w3c.dom.Text;
import program.Main;
import program.models.User;
import program.utils.RestAPI;
import program.utils.StringToMap;

public class AdminPageController {

    @FXML
    private TextField adminFirstName;

    @FXML
    private TextField adminLastName;

    @FXML
    private TextField adminLogin;

    @FXML
    private TextField adminNewPassword;

    @FXML
    private TextField adminRepeatedPassword;

    private Main main;
    private RestAPI restAPI;
    private AnchorPane anchorPane;
    private StringToMap stringToMap;
    private User admin;

    public void setMain(User admin, Main main, RestAPI restAPI, AnchorPane anchorPane, StringToMap stringToMap) {
        this.admin = admin;
        this.main = main;
        this.restAPI = restAPI;
        this.anchorPane = anchorPane;
        this.stringToMap = stringToMap;
    }

    @FXML
    private void initialize() {
        adminLogin.setEditable(false);
    }

    public void setAdmin(User admin){
        adminFirstName.setText(admin.getFirstName());
        adminLastName.setText(admin.getLastName());
        adminLogin.setText(admin.getLogin());
        adminNewPassword.setText("");
        adminRepeatedPassword.setText("");
    }

    @FXML
    public void adminSave(){
        System.out.println("adminSave is clicked");
    }

    @FXML
    public void logOut(){
        //TODO: прописать logout в restAPI
        main.hideOverview(anchorPane);
        main.showAuthorizationForm();
    }


}
