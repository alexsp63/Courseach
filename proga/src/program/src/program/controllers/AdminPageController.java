package program.controllers;


import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import program.Main;
import program.models.Lesson;
import program.models.User;
import program.utils.RestAPI;

import java.io.IOException;

import static program.controllers.SignUpController.isDouble;
import static program.controllers.SignUpController.isInteger;

/**
 * Контроллер, отвечающий за окно администратора (adminMainPage.fxml)
 */
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

    @FXML
    private Label userOkMessage;

    @FXML
    private TableView<Lesson> lessonTable;

    @FXML
    private TableColumn<Lesson, String> lessonColumnName;

    @FXML
    private Label lessonName;

    @FXML
    private Label lessonText;

    @FXML
    private Label lessonQuestionType;

    @FXML
    private Button userSaveButton;

    @FXML
    private Button userClearButton;

    @FXML
    private Button lessonQuestionsButton;

    @FXML
    private Button lessonEditButton;

    @FXML
    private Button lessonDeleteButton;

    @FXML
    private TableView<User> usTable;

    @FXML
    private TableColumn<User, String> usLogin;

    @FXML
    private TableColumn<User, String> usFirstName;

    @FXML
    private TableColumn<User, String> usLastName;

    @FXML
    private TextField searchField;

    @FXML
    private ChoiceBox<String> chosenType;

    @FXML
    private ChoiceBox<String> chooseStatType;

    @FXML
    private Group userGroup;

    @FXML
    private TabPane adminTab;

    @FXML
    private Group lessonGroup;

    @FXML
    private TableView<Lesson> lessTable;

    @FXML
    private TableColumn<Lesson, String> lessName;

    @FXML
    private TableColumn<Lesson, String> lessQuestionType;

    @FXML
    private TextField lessonSearchField;

    @FXML
    private AnchorPane adminAnchorPane;

    private FilteredList<User> filteredList;
    private FilteredList<Lesson> filteredLessonList;

    private Main main;
    private RestAPI restAPI;
    private AnchorPane anchorPane;
    private User admin;
    private String token;

    /**
     * Инициализация из Main'а
     * @param admin - администратор, который зашёл в систему
     * @param main - maim
     * @param restAPI - restAPI
     * @param anchorPane - форма окна администратора
     * @param token - токен залогиневшегося админа
     */
    public void setMain(User admin, Main main, RestAPI restAPI, AnchorPane anchorPane, String token) {
        this.admin = admin;
        this.main = main;
        this.restAPI = restAPI;
        this.anchorPane = anchorPane;
        this.token = token;

        userTable.setItems(main.getUserData());
        lessonTable.setItems(main.getLessonData());

        filteredList = new FilteredList(main.getUserData(), p -> true);
        usTable.setItems(filteredList);

        filteredLessonList = new FilteredList(main.getLessonData(), p -> true);
        lessTable.setItems(filteredLessonList);

        main.createAppearEffect(anchorPane,2);
    }

    /**
     * Инициализация элементов формы:
     * Задание стилей и поведения TextField'ам,
     * Обработка динамического отображения элементов в таблице при поиске,
     * Добавление элементов ComboBox'ов, ChoiceBox'ов,
     * Прописано поведение таблиц (в том числе при выборе строки)
     * @throws NullPointerException - исключение
     */
    @FXML
    private void initialize() throws NullPointerException{

        TextField[] textFields = {adminFirstName, adminLastName, adminNewPassword, adminRepeatedPassword};
        for (TextField textField: textFields){
            textField.setOnMouseClicked(mouseEvent -> {
                textField.setStyle("-fx-border-color: #14a9ff; -fx-border-width: 0 0 2 0;");
                for (TextField textField1: textFields){
                    if (textField1 != textField){
                        textField1.setStyle("-fx-border-color: #84cdff; -fx-border-width: 0 0 2 0;");
                    }
                }
            });
        }

        adminLogin.setEditable(false);
        userFirstName.setEditable(false);
        userLastName.setEditable(false);
        userLogin.setEditable(false);

        chosenType.getItems().addAll("Поиск по логину",
                "Поиск по имени",
                "Поиск по фамилии");
        chosenType.getSelectionModel().select(0);

        userSaveButton.setDisable(true);
        userClearButton.setDisable(true);

        lessonQuestionsButton.setDisable(true);
        lessonEditButton.setDisable(true);
        lessonDeleteButton.setDisable(true);

        chooseStatType.getItems().addAll("Отобразить по пользователю", "Отобразить по уроку");
        chooseStatType.getSelectionModel().select(0);

        lessonGroup.setVisible(false);
        lessonGroup.setDisable(true);

        searchField.setPromptText("Поиск по логину");

        chosenType.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                if (chosenType.getItems().get((Integer) number2) == "Поиск по логину"){
                    searchField.setPromptText("Введите логин");
                }
                if (chosenType.getItems().get((Integer) number2) == "Поиск по имени"){
                    searchField.setPromptText("Введите имя");
                }
                if (chosenType.getItems().get((Integer) number2) == "Поиск по фамилии"){
                    searchField.setPromptText("Введите фамилию");
                }
            }
        });

        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (chosenType.getValue().equals("Поиск по логину")){
                filteredList.setPredicate(p -> p.getLogin().toLowerCase().contains(newValue.toLowerCase().trim()));
            } else if (chosenType.getValue().equals("Поиск по имени")) {
                filteredList.setPredicate(p -> p.getFirstName().toLowerCase().contains(newValue.toLowerCase().trim()));
            } else if (chosenType.getValue().equals("Поиск по фамилии")) {
                filteredList.setPredicate(p -> p.getLastName().toLowerCase().contains(newValue.toLowerCase().trim()));
            }
        });

        chooseStatType.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                if (chooseStatType.getItems().get((Integer) number2) == "Отобразить по пользователю"){
                    userGroup.setVisible(true);
                    userGroup.setDisable(false);
                    lessonGroup.setVisible(false);
                    lessonGroup.setDisable(true);
                }
                if (chooseStatType.getItems().get((Integer) number2) == "Отобразить по уроку"){
                    userGroup.setVisible(false);
                    userGroup.setDisable(true);
                    lessonGroup.setVisible(true);
                    lessonGroup.setDisable(false);
                }
            }
        });

        lessonSearchField.textProperty().addListener((obs, oldValue, newValue) -> {
            filteredLessonList.setPredicate(p -> p.getName().toLowerCase().contains(newValue.toLowerCase().trim()));
        });

        userFirstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        userLastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        lessonColumnName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        usLogin.setCellValueFactory(cellData -> cellData.getValue().loginProperty());
        usFirstName.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        usLastName.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        lessName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        lessQuestionType.setCellValueFactory(cellData -> cellData.getValue().questionTypeProperty());

        showUserDetails(null);
        showLessonDetails(null);

        userTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, olValue, newValue) -> showUserDetails(newValue)
        );

        lessonTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, olValue, newValue) -> showLessonDetails(newValue)
        );

        usTable.setOnMouseClicked(event -> {
            int index = usTable.getSelectionModel().getSelectedIndex();
            User curUser = usTable.getSelectionModel().getSelectedItem();
            usTable.getSelectionModel().clearSelection(index);
            showStatForAdmin(curUser);
        });

        lessTable.setOnMouseClicked(event -> {
            int index = lessTable.getSelectionModel().getSelectedIndex();
            Lesson curLesson = lessTable.getSelectionModel().getSelectedItem();
            lessTable.getSelectionModel().clearSelection(index);
            showStatForAdmin(curLesson);
        });

        userRole.getItems().addAll("USER", "ADMIN");
        userStatus.getItems().addAll("ACTIVE", "BANNED");
    }

    /**
     * Установка информации в личный кабинет администратора
     * @param admin - текущий авторизованный в системе администратор
     */
    public void setAdmin(User admin){
        adminFirstName.setText(admin.getFirstName());
        adminLastName.setText(admin.getLastName());
        adminLogin.setText(admin.getLogin());
        adminNewPassword.setText("");
        adminRepeatedPassword.setText("");
    }

    /**
     * Отображение информации о пользователе сбоку от таблицы пользователей
     * @param user - выбранная строчка в таблице
     */
    public void showUserDetails(User user){
        if (user != null) {
            userSaveButton.setDisable(false);
            userClearButton.setDisable(false);
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

    /**
     * Отображение информации об уроке сбоку от таблицы
     * @param lesson - выбранная строчка таблицы уроков
     */
    public void showLessonDetails(Lesson lesson){
        if (lesson != null){
            lessonQuestionsButton.setDisable(false);
            lessonEditButton.setDisable(false);
            lessonDeleteButton.setDisable(false);
            lessonName.setText(lesson.getName());
            lessonText.setText(lesson.getTextText());
            lessonQuestionType.setText(lesson.getQuestionType());
        } else {
            lessonQuestionsButton.setDisable(true);
            lessonEditButton.setDisable(true);
            lessonDeleteButton.setDisable(true);
            lessonName.setText("");
            lessonText.setText("");
            lessonQuestionType.setText("");
        }
    }

    /**
     * Показ формы статистики для администратора (по выбранному пользователю)
     * @param user - выбранный пользователь
     */
    public void showStatForAdmin(User user){

        main.showStatAdminForm(token, user, null);
    }

    /**
     *Показ формы статистики для администратора (по выбранному уроку)
     * @param lesson - выбранный урок
     */
    public void showStatForAdmin(Lesson lesson){

        main.showStatAdminForm(token, null, lesson);

    }

    /**
     * Формирование сообщения об ошибке при валидации введённых в личном кабинете данных
     * @return строку сообщения об ошибке
     */
    private String adminErrorMessage(){
        if (adminFirstName.getText() == null || isDouble(adminFirstName.getText()) == true
                || isInteger(adminFirstName.getText()) == true || adminFirstName.getText().length() == 0
                || adminFirstName.getText().equals("")){
            SignUpController.makeRed(adminFirstName);
            return "Недопустимый формат имени!";
        }
        if (adminLastName.getText() == null || isDouble(adminLastName.getText()) == true
                || isInteger(adminLastName.getText()) == true || adminLastName.getText().length() == 0
                || adminLastName.getText().equals("")){
            SignUpController.makeRed(adminLastName);
            return "Недопустимый формат фамилии!";
        }
        if (adminNewPassword.getText() != null || !adminNewPassword.getText().equals("")) {
            if (!adminRepeatedPassword.getText().equals(adminNewPassword.getText())){
                SignUpController.makeRed(adminNewPassword);
                SignUpController.makeRed(adminRepeatedPassword);
                return "Подтвердите новый пароль!";
            }
        }
        return "";
    }

    /**
     * Нажатие на кнопку сохранения введённых изменений в личном кабинете
     */
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
                UserPageController.setErrorMessage(adminErrorMessage, main, token);
            }
        }
    }

    /**
     * Нажатие на кнопку отмены изменений в личном кабинете
     */
    @FXML
    public void adminCancel(){
        setAdmin(admin);
    }

    /**
     * Нажатие на кнопку отмены изменений в окне редактирования пользователей
     */
    @FXML
    public void userCancel(){
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        showUserDetails(selectedUser);
    }

    /**
     * Нажатие на кнопку сохранения выбранных изменений в окне редактирования пользователей
     */
    @FXML
    public void userSave(){
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null){
            User oldUser = new User(selectedUser.getLogin(),
                                    selectedUser.getPassword(),
                                    selectedUser.getFirstName(),
                                    selectedUser.getLastName(),
                                    selectedUser.getRole(),
                                    selectedUser.getStatus());
            selectedUser.setRole(userRole.getValue());
            selectedUser.setStatus(userStatus.getValue());
            if (oldUser.getRole() != selectedUser.getRole() || oldUser.getStatus() != selectedUser.getStatus()) {
                userOkMessage.setTextFill(Color.web("green"));
                userOkMessage.setText("Информация обновлена");
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(e -> userOkMessage.setText(""));
                pause.play();
                restAPI.putUser(selectedUser, token);
        }
        }
    }

    /**
     * Показ вопросов выбранного из таблицы урока
     */
    @FXML
    public void showQuestions(){
        Lesson selectedLesson = lessonTable.getSelectionModel().getSelectedItem();
        if (selectedLesson != null) {
            main.showLessonQuestions(token, selectedLesson);

        }
    }

    /**
     * Нажатие на кнопку добавления урока
     * @throws IOException - исключение
     */
    @FXML
    public void addLesson() throws IOException {
        //тут нам не важно, выбрана какая-то запись или нет: новый lesson есть новый lesson
        Lesson newLesson = new Lesson();
        boolean okIsClicked = main.showLessonAddEditForm(newLesson, token);
        if (okIsClicked){
            //значит, все данные заполнены корректно, и можно добавлять его к остальным
            restAPI.postLesson(newLesson, token);
            main.updateLessonTable(token);
        }
    }

    /**
     * Нажатие на кнопку изменения урока
     * @throws IOException - исключение
     */
    @FXML
    public void editLesson() throws IOException {
        Lesson selectedLesson = lessonTable.getSelectionModel().getSelectedItem();
        if (selectedLesson != null) {
            boolean okIsClicked = main.showLessonAddEditForm(selectedLesson, token);
            if (okIsClicked) {
                restAPI.putLesson(selectedLesson, token);
                main.updateLessonTable(token);
                showLessonDetails(selectedLesson);
            }
        }
    }

    /**
     * Нажатие на кнопку удаления уроков
     */
    @FXML
    public void deleteLesson(){
        Lesson selectedLesson = lessonTable.getSelectionModel().getSelectedItem();
        if (selectedLesson != null){
            if (restAPI.deleteLesson(selectedLesson, token)){
                lessonTable.getItems().remove(selectedLesson);
            }
        }
    }

    /**
     * Проверка ввода новых данных в личном кабинете
     * @return true при корректном вводе, false и сообщение об ошибке - при некорректном
     */
    private boolean adminInputCheck() {
        String message = adminErrorMessage();
        if (message.length() == 0) {
            adminErrorMessage.setText("");
            return true;
        } else {
            adminErrorMessage.setTextFill(Color.web("red"));
            adminErrorMessage.setText(message);
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(e -> adminErrorMessage.setText(""));
            pause.play();
            return false;
        }
    }

    /**
     * Нажатие на кнопку "Выйти"
     */
    @FXML
    public void logOut(){
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
                main.showAuthorizationForm();
            }
        });
        new Thread(sleeper).start();
    }

    /**
     * Нажатие на кнопку показа справки о программе для администратора
     */
    @FXML
    private void showInfo(){
        main.showInfo("admin");
    }
}
