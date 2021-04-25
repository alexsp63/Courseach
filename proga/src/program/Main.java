package program;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
import program.controllers.*;
import program.models.Lesson;
import program.models.Question;
import program.models.Statistics;
import program.models.User;
import program.utils.RestAPI;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Главный класс приложения
 */
public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private RestAPI restAPI;
    private ObservableList<User> userData = FXCollections.observableArrayList();
    private ObservableList<Lesson> lessonData = FXCollections.observableArrayList();
    private ObservableList<Question> questions4Data = FXCollections.observableArrayList();
    private ObservableList<Statistics> statisticsData = FXCollections.observableArrayList();

    /**
     * Конструктор - установка переменной restAPI, объекта класса RestAPI
     */
    public Main() {

        restAPI = new RestAPI();
    }

    /**
     * Точко входа в приложение
     * @param primaryStage - главная сцена приложения, на которую будут помещаться разные scene
     * @throws Exception - исключение
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("English Slang Application");
        primaryStage.getIcons().add(new Image("program/style/images/AppIcon.png"));  //установка иконки приложения

        initRootLayout(); //сначала показываем root

        showAuthorizationForm(); //потом форму авторизации
    }

    /**
     * Метод сокрытия окна с анимацией
     * @param anchorPane - панель, которую нужно плавно закрыть
     * @param sec - число секунд, в течение которого окно должно плавно закрываться
     */
    public void hideOverview(AnchorPane anchorPane, double sec) {
        KeyValue initKeyValue = new KeyValue(anchorPane.opacityProperty(), 1.0);
        KeyFrame initFrame = new KeyFrame(Duration.ZERO, initKeyValue);
        KeyValue endKeyValue = new KeyValue(anchorPane.opacityProperty(), 0.0);
        KeyFrame endFrame = new KeyFrame(Duration.seconds(sec), endKeyValue);
        Timeline timeline = new Timeline(initFrame, endFrame);
        timeline.setCycleCount(1);
        timeline.play();
    }

    /**
     * Эффект появления окна - плавное выцветание
     * @param anchorPane - окно, которое должно плавно появиться
     * @param sec - число секунд, в течение которого окно должно выцветать
     */
    public void createAppearEffect(AnchorPane anchorPane, double sec){
        KeyValue initKeyValue = new KeyValue(anchorPane.opacityProperty(), 0.0);
        KeyFrame initFrame = new KeyFrame(Duration.ZERO, initKeyValue);
        KeyValue endKeyValue = new KeyValue(anchorPane.opacityProperty(), 1.0);
        KeyFrame endFrame = new KeyFrame(Duration.seconds(sec), endKeyValue);
        Timeline timeline = new Timeline(initFrame, endFrame);
        timeline.setCycleCount(1);
        timeline.play();
    }

    /**
     * Обновление таблицы пользователей
     * @param token - токен текущего пользователя для осуществления get-запроса
     */
    public void updateUserTable(String token){
        userData.clear();
        userData.addAll(restAPI.getUsers(token));
    }

    /**
     * Обновление таблицы уроков
     * @param token - токен текущего пользователя для осуществления get-запроса
     */
    public void updateLessonTable(String token){
        lessonData.clear();
        lessonData.addAll(restAPI.getLessons(token));
    }

    /**
     * Обновление таблицы вопросов
     * @param token - токен текущего пользователя
     * @param lesson - урок, по которому нужно найти все вопросы
     */
    public void updateQuestionData(String token, Lesson lesson){
        questions4Data.clear();
        List<Question> questions = restAPI.getQuestionsByLesson(token, lesson);

        questions4Data.addAll(questions);
    }

    /**
     * Обновление таблицы статистики
     * @param token - токен текущего пользователя
     * @param lesson - урок, по которому нужно найти статистику
     * @param user - пользователь, по которому нужно найти статистику
     */
    public void updateStatisticsData(String token, Lesson lesson, User user){
        statisticsData.clear();
        List<Statistics> statistics = restAPI.getStatiscticsByLessonAndUser(token, lesson, user);

        statisticsData.addAll(statistics);
    }

    /**
     * Загрузка картинки заднего фона, это главная сцена
     */
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

    /**
     * Показ формы авторизации
     */
    public void showAuthorizationForm() {

        //загрузка формы авторизация
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("views/authorizationForm.fxml"));
            AnchorPane authorization = (AnchorPane) loader.load();


            rootLayout.setCenter(authorization);  //устанавливаем форму авторизации

            AuthorizationController controller = loader.getController();
            controller.setMain(this, restAPI, authorization);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Показ формы регистрации
     * @return true, если регистрация прошла успешно, иначе - false
     */
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

    /**
     * Показ главного окна администратора
     * @param admin - администратор, авторизовавшийся в системе
     * @param token - токен текущего администратора
     */
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

    /**
     * Главное окно пользователя
     * @param user - пользователь, работающий в ситеме в данный момент
     * @param token - токен текущего пользователя
     */
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

    /**
     * Показ вопросов к уроку
     * @param token - токен человека, работающего в системе
     * @param lesson - урок, по которому надо показать вопросы
     */
    public void showLessonQuestions(String token, Lesson lesson){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("views/lessonQuestions.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogueStage = new Stage();
            dialogueStage.getIcons().add(new Image("program/style/images/lessonIcon.png"));
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

    /**
     * Форма добавления/изменения уроков
     * @param lesson - урок, который надо изменить, если добавляем, то null
     * @param token - токен администратора, желающего добавить/изменить урок
     * @return true, если урок добавлен/обновлён, иначе false
     * @throws IOException - исключение
     */
    public boolean showLessonAddEditForm(Lesson lesson, String token) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("views/lessonAddEditForm.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage lessonStage = new Stage();
            lessonStage.getIcons().add(new Image("program/style/images/editLesson.png"));
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

    /**
     * Форма изменения/добавления вопроса
     * @param question - вопрос, который надо изменить, если добавить - то null
     * @param token - токен администратора, авторизованного в системе
     * @param lesson - урок, к которому принадлежить изменяемый/добавляемый вопрос
     * @return true, если вопрос добавлен/изменён, иначе false
     */
    public boolean showQuestionAddEditForm(Question question, String token, Lesson lesson){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("views/questionsAddEditForm.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage questionStage = new Stage();
            questionStage.getIcons().add(new Image("program/style/images/testIcon.png"));
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

    /**Окно прохождения теста пользователем
     * @param token - токен пользователя, проходящего тест
     * @param questions - список вопросов
     * @param lesson - урок, по которому проходится тест
     * @param user - пользователь, проходящий тест
     */
    public void showTestWindow(String token, List<Question> questions, Lesson lesson, User user){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("views/testWindow.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage testStage = new Stage();
            testStage.getIcons().add(new Image("program/style/images/testIcon.png"));
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

    /**
     * Показ статистики пользователю
     * @param token - строка токена текущего пользователя системы
     * @param lesson - урок, по которому будет отображена статистика
     * @param user - пользователь, по которому будет отображена статистика
     */
    public void showStatUserForm(String token, Lesson lesson, User user){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("views/statUserForm.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage statUserStage = new Stage();
            statUserStage.getIcons().add(new Image("program/style/images/statistics.png"));
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

    /**
     * Окно показа статистики для администратора
     * @param token - токен администратора
     * @param user - пользователь, по которому будет отображена статистика
     * @param lesson - урок, по которому будет отображена статистика
     */
    public void showStatAdminForm(String token, User user, Lesson lesson){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("views/statAdminForm.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage statAdminStage = new Stage();
            statAdminStage.getIcons().add(new Image("program/style/images/statistics.png"));
            if (user != null) {
                statAdminStage.setTitle("Результаты пользователя " + user.getLogin());
            } else if (lesson != null){
                statAdminStage.setTitle("Результаты по уроку " + lesson.getName());
            }
            statAdminStage.initModality(Modality.WINDOW_MODAL);
            statAdminStage.initOwner(primaryStage);

            Scene scene = new Scene(page);
            statAdminStage.setScene(scene);
            StatAdminController controller = loader.getController();

            controller.setMain(statAdminStage, this, restAPI, token, user, lesson);

            statAdminStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Окно информации
     * @param type - тип информации, который надо отобразить
     */
    public void showInfo(String type){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("views/info.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage infoStage = new Stage();
            infoStage.getIcons().add(new Image("program/style/images/info.png"));
            if (type == "author") {
                infoStage.setTitle("Об авторе");
            } else if (type == "admin"){
                infoStage.setTitle("Руководство для администратора");
            } else if (type == "user"){
                infoStage.setTitle("Руководство для пользователя");
            }
            infoStage.initModality(Modality.WINDOW_MODAL);
            infoStage.initOwner(primaryStage);

            Scene scene = new Scene(page);
            infoStage.setScene(scene);
            InfoController controller = loader.getController();

            controller.setInfo(type);

            infoStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Получение статистики
     * @return список статистики
     */
    public ObservableList<Statistics> getStatisticsData() {
        return statisticsData;
    }

    /**
     * Получение пользовательской информации
     * @return список пользователей
     */
    public ObservableList<User> getUserData() {
        return userData;
    }

    /**
     * Получение информации по урокам
     * @return список уроков
     */
    public ObservableList<Lesson> getLessonData() {
        return lessonData;
    }

    /**
     * Получение информации о вопросах
     * @return список вопросов
     */
    public ObservableList<Question> getQuestionsData() {
        return questions4Data;
    }

    /**
     * Получение главной Stage приложения
     * @return главную Stage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Для работы с сервером
     * @return объект класса RestAPI
     */
    public RestAPI getRestAPI() {
        return restAPI;
    }

    /**
     * точка входа в приложение
     * @param args - аругменты командной строки
     */
    public static void main(String[] args) {
        launch(args);
    }
}

