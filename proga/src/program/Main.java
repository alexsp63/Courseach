package program;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import program.controllers.*;
import program.models.Lesson;
import program.models.Question;
import program.models.Statistics;
import program.models.User;
import program.utils.DateUtil;
import program.utils.RestAPI;
import program.utils.StringToMap;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private RestAPI restAPI;
    private StringToMap stringToMap;
    private ObservableList<User> userData = FXCollections.observableArrayList();
    private ObservableList<Lesson> lessonData = FXCollections.observableArrayList();
    private ObservableList<Question> questions4Data = FXCollections.observableArrayList();
    private ObservableList<Statistics> statisticsData = FXCollections.observableArrayList();

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

    public void updateUserTable(String token){
        userData.clear();
        userData.addAll(restAPI.getUsers(token));
    }

    public void updateLessonTable(String token){
        lessonData.clear();
        //System.out.println(restAPI.getLessons(token));
        lessonData.addAll(restAPI.getLessons(token));
    }

    public void updateQuestionData(String token, Lesson lesson){
        questions4Data.clear();
        //System.out.println(restAPI.getQuestionsByLesson(token, lesson));
        List<Question> questions = restAPI.getQuestionsByLesson(token, lesson);

        questions4Data.addAll(questions);
    }

    public void updateStatisticsData(String token, Lesson lesson, User user){
        statisticsData.clear();
        List<Statistics> statistics = restAPI.getStatiscticsByLessonAndUser(token, lesson, user);

        statisticsData.addAll(statistics);
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
            controller.setMain(admin, this, restAPI, adminForm, token);
            controller.setAdmin(admin);
            updateUserTable(token);
            updateLessonTable(token);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showUserForm(User user, String token){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("views/userMainPage.fxml"));
            AnchorPane userForm = (AnchorPane) loader.load();

            rootLayout.setCenter(userForm);

            UserPageController controller = loader.getController();
            controller.setMain(user, this, restAPI, userForm, token);
            controller.setUser(user);
            updateLessonTable(token);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showLessonQuestions(String token, Lesson lesson){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("views/lessonQuestions.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogueStage = new Stage();
            dialogueStage.setTitle("Вопросы урока " + lesson.getName());
            dialogueStage.initModality(Modality.WINDOW_MODAL);
            dialogueStage.initOwner(primaryStage);

            Scene scene = new Scene(page);
            dialogueStage.setScene(scene);
            LessonQuestionsController controller = loader.getController();

            controller.setStage(dialogueStage, this, restAPI, lesson, token);
            updateQuestionData(token, lesson);

            dialogueStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showLessonAddEditForm(Lesson lesson, String token) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("views/lessonAddEditForm.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage lessonStage = new Stage();
            lessonStage.setTitle("Ввод данных урока");
            lessonStage.initModality(Modality.WINDOW_MODAL);
            lessonStage.initOwner(primaryStage);

            Scene scene = new Scene(page);
            lessonStage.setScene(scene);
            LessonAddEditController controller = loader.getController();

            controller.setStage(lessonStage, this, restAPI, token);
            controller.setLesson(lesson);

            lessonStage.showAndWait();

            return controller.isSaveIsClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showQuestionAddEditForm(Question question, String token, Lesson lesson){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("views/questionsAddEditForm.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage questionStage = new Stage();
            questionStage.setTitle("Вопрос урока " + lesson.getName());
            questionStage.initModality(Modality.WINDOW_MODAL);
            questionStage.initOwner(primaryStage);

            Scene scene = new Scene(page);
            questionStage.setScene(scene);
            QuestionsAddEditController controller = loader.getController();

            controller.setStage(questionStage, this, restAPI, token, lesson);
            controller.setQuestion(question);

            questionStage.showAndWait();

            return controller.isSaveIsClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void showTestWindow(String token, List<Question> questions, Lesson lesson, User user){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("views/testWindow.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage testStage = new Stage();
            testStage.setTitle("Тест урока " + lesson.getName());
            testStage.initModality(Modality.WINDOW_MODAL);
            testStage.initOwner(primaryStage);

            Scene scene = new Scene(page);
            testStage.setScene(scene);
            TestWindowController controller = loader.getController();
            Collections.shuffle(questions);

            controller.setStage(testStage, this, restAPI, token, questions, user, lesson);

            testStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStatUserForm(String token, Lesson lesson, User user){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("views/statUserForm.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage statUserStage = new Stage();
            statUserStage.setTitle("Результаты пользователя " + user.getLogin() + " по уроку " + lesson.getName());
            statUserStage.initModality(Modality.WINDOW_MODAL);
            statUserStage.initOwner(primaryStage);

            Scene scene = new Scene(page);
            statUserStage.setScene(scene);
            StatUserController controller = loader.getController();

            controller.setMain(statUserStage, this, restAPI, token, lesson, user);

            statUserStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Statistics> getStatisticsData() {
        return statisticsData;
    }

    public ObservableList<User> getUserData() {
        return userData;
    }

    public ObservableList<Lesson> getLessonData() {
        return lessonData;
    }

    public ObservableList<Question> getQuestionsData() {
        return questions4Data;
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

