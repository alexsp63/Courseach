package program;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import program.controllers.AuthorizationController;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("English Slang Application");
        primaryStage.getIcons().add(new Image("program/style/images/AppIcon.png"));  //установка иконки приложения

        initRootLayout(); //сначала показываем root

        showAuthorizationForm(); //потом форму авторизации
    }

    public void initRootLayout() {

        //загрузка фона
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("views/root.fxml"));
            rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAuthorizationForm() {

        //загрузка формы авторизация
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("views/authorization.fxml"));
            AnchorPane authorization = (AnchorPane) loader.load();

            rootLayout.setBottom(authorization);  //устанавливаем форму авторизации по нижнему краю

            AuthorizationController controller = loader.getController();
            controller.setMain(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

