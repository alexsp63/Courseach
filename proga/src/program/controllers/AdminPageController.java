package program.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.*;
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

    @FXML
    private TextField userFirstName;

    @FXML
    private TextField userLastName;

    @FXML
    private TextField userLogin;

    @FXML
    private ComboBox<String> userRole;

    @FXML
    private ComboBox<String> userStatus;

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, String> userFirstNameColumn;

    @FXML
    private TableColumn<User, String> userLastNameColumn;

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

        userTable.setItems(main.getUserData());
    }

    @FXML
    private void initialize() {

        adminLogin.setEditable(false);
        userFirstName.setEditable(false);
        userLastName.setEditable(false);
        userLogin.setEditable(false);

        userFirstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        userLastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        showUserDetails(null);

        userTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, olValue, newValue) -> showUserDetails(newValue)
        );

        userRole.getItems().addAll("USER", "ADMIN");
        userStatus.getItems().addAll("ACTIVE", "BANNED");
    }

    public void setAdmin(User admin){
        adminFirstName.setText(admin.getFirstName());
        adminLastName.setText(admin.getLastName());
        adminLogin.setText(admin.getLogin());
        adminNewPassword.setText("");
        adminRepeatedPassword.setText("");
    }

    public void showUserDetails(User user){
        if (user != null) {
            userFirstName.setText(user.getFirstName());
            userLastName.setText(user.getLastName());
            userLogin.setText(user.getLogin());
            userRole.valueProperty().setValue(user.getRole());
            userStatus.valueProperty().setValue(user.getStatus());
        } else {
            userFirstName.setText("");
            userLastName.setText("");
            userLogin.setText("");
            userRole.setPromptText("");
            userStatus.setPromptText("");
        }
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

    @FXML
    public void userCancel(){
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        showUserDetails(selectedUser);
    }

    @FXML
    public void userSave(){
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        User oldUser = new User(selectedUser.getLogin(),
                                selectedUser.getPassword(),
                                selectedUser.getFirstName(),
                                selectedUser.getLastName(),
                                selectedUser.getRole(),
                                selectedUser.getStatus());
        selectedUser.setRole(userRole.getValue());
        selectedUser.setStatus(userStatus.getValue());
        if (oldUser.getRole() != selectedUser.getRole() || oldUser.getStatus() != selectedUser.getStatus()){
            restAPI.putUser(selectedUser, token);
        }
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
