package program.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
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

    @FXML
    private Label adminErrorMessage;

    private Main main;
    private RestAPI restAPI;
    private AnchorPane anchorPane;
    private StringToMap stringToMap;
    private User admin;
    private String token;

    public void setMain(User admin, Main main, RestAPI restAPI, AnchorPane anchorPane, StringToMap stringToMap, String token) {
        this.admin = admin;
        this.main = main;
        this.restAPI = restAPI;
        this.anchorPane = anchorPane;
        this.stringToMap = stringToMap;
        this.token = token;
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

    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String adminErrorMessage(){
        if (adminFirstName.getText() == null || isDouble(adminFirstName.getText()) == true
                || isInteger(adminFirstName.getText()) == true || adminFirstName.getText().length() == 0
                || adminFirstName.getText().equals("")){
            return "Недопустимый формат имени!";
        }
        if (adminLastName.getText() == null || isDouble(adminLastName.getText()) == true
                || isInteger(adminLastName.getText()) == true || adminLastName.getText().length() == 0
                || adminLastName.getText().equals("")){
            return "Недопустимый формат фамилии!";
        }
        if (adminNewPassword.getText() != null || !adminNewPassword.getText().equals("")) {
            if (!adminRepeatedPassword.getText().equals(adminNewPassword.getText())){
                return "Подтвердите новый пароль!";
            }
        }
        return "";
    }

    @FXML
    public void adminSave(){
        if (adminInputCheck()){
            adminErrorMessage.setText("");
            User oldAdmin = new User(admin.getLogin(),
                                     admin.getPassword(),
                                     admin.getFirstName(),
                                     admin.getLastName(),
                                     admin.getRole(),
                                     admin.getStatus());
            admin.setFirstName(adminFirstName.getText());
            admin.setLastName(adminLastName.getText());
            if (!adminNewPassword.equals("") && adminNewPassword.getText() != null && adminNewPassword.getText().length() != 0){
                admin.setPassword(adminNewPassword.getText());
            }
            if (oldAdmin.getFirstName() != admin.getFirstName() ||
                oldAdmin.getLastName() != admin.getLastName() ||
                oldAdmin.getPassword() != admin.getPassword()){
                restAPI.putUser(admin, token);
                setAdmin(admin);
            }
        }
    }

    @FXML
    public void adminCancel(){
        setAdmin(admin);
    }

    private boolean adminInputCheck() {
        String message = adminErrorMessage();
        if (message.length() == 0) {
            adminErrorMessage.setText("");
            return true;
        } else {
            adminErrorMessage.setText(message);
            return false;
        }
    }

    @FXML
    public void logOut(){
        //TODO: прописать logout в restAPI
        main.hideOverview(anchorPane);
        main.showAuthorizationForm();
    }


}
