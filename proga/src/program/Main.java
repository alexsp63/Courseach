package program;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import program.controllers.AdminPageController;
import program.controllers.AuthorizationController;
import program.controllers.SignUpController;
import program.models.User;
import program.utils.RestAPI;
import program.utils.StringToMap;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private RestAPI restAPI;
    private StringToMap stringToMap;


    public Main() {

        restAPI = new RestAPI();
        stringToMap = new StringToMap();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("English Slang Application");
        primaryStage.getIcons().add(new Image("program/style/images/AppIcon.png"));  //установка иконки приложения

        initRootLayout(); //сначала показываем root

        showAuthorizationForm(); //потом форму авторизации
    }

    public void hideOverview(AnchorPane form) {
        form.setVisible(false);
    }

    public void showOverview(AnchorPane form) {
        form.setVisible(true);
    }

    public void initRootLayout() {

        //загрузка фона
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("views/root.fxml"));
            rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout, Color.TRANSPARENT);
            primaryStage.setScene(scene);
            //primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAuthorizationForm() {

        //загрузка формы авторизация
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("views/authorizationForm.fxml"));
            AnchorPane authorization = (AnchorPane) loader.load();

            rootLayout.setCenter(authorization);  //устанавливаем форму авторизации


            AuthorizationController controller = loader.getController();
            controller.setMain(this, restAPI, authorization, stringToMap);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showSignUpForm(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("views/signUpForm.fxml"));
            AnchorPane signUp = (AnchorPane) loader.load();

            rootLayout.setCenter(signUp);

            SignUpController controller = loader.getController();
            controller.setMain(this, restAPI, signUp);

            return controller.isCloseISClicked();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void showAdminForm(User admin, String token){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("views/adminMainPage.fxml"));
            AnchorPane adminForm = (AnchorPane) loader.load();

            rootLayout.setCenter(adminForm);

            AdminPageController controller = loader.getController();
            controller.setMain(admin, this, restAPI, adminForm, stringToMap, token);
            controller.setAdmin(admin);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public RestAPI getRestAPI() {
        return restAPI;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

