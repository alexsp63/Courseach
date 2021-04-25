package program.models;

import com.google.gson.Gson;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Модель пользователя
 */
public class User implements JSONSerialize{

    private StringProperty login;
    private StringProperty password;
    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty role;
    private StringProperty status;

    /**
     * Пустой конструктор
     */
    public User(){
        this(null, null, null, null);
    }

    /**
     * Конструктор создания формирования пользователя для отправки на сервер
     * @param login - логин
     * @param password - пароль
     * @param firstName - имя
     * @param lastName - фамилия
     */
    public User(String login, String password, String firstName, String lastName) {
        this.login = new SimpleStringProperty(login);
        this.password = new SimpleStringProperty(password);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.role = new SimpleStringProperty("USER");
        this.status = new SimpleStringProperty("ACTIVE");
    }

    /**
     * Конструктор получения пользователя от сервера
     * @param login - логин
     * @param password - пароль
     * @param firstName - имя
     * @param lastName - фамилия
     * @param role - роль
     * @param status - статус
     */
    public User(String login, String password, String firstName, String lastName, String role, String status) {
        this.login = new SimpleStringProperty(login);
        this.password = new SimpleStringProperty(password);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.role = new SimpleStringProperty(role);
        this.status = new SimpleStringProperty(status);
    }

    /**
     * Получение имени пользователя
     * @return имя пользователя
     */
    public String getFirstName() {
        return firstName.get();
    }

    /**
     * Получение фамилии пользователя
     * @return фамилию пользователя
     */
    public String getLastName() {
        return lastName.get();
    }

    /**
     * Получение пароля пользователя
     * @return пароль
     */
    public String getPassword() {
        return password.get();
    }

    /**
     * Получение статуса пользователя
     * @return статус
     */
    public String getStatus() {
        return status.get();
    }

    /**
     * Установка роли пользователю
     * @param role роль
     */
    public void setRole(String role) {
        this.role.set(role);
    }

    /**
     * Установка статуса пользователю
     * @param status статус
     */
    public void setStatus(String status) {
        this.status.set(status);
    }

    /**
     * Получение обертки логина
     * @return логин
     */
    public StringProperty loginProperty() {
        return login;
    }

    /**
     * Получение обёртки имени
     * @return имя
     */
    public StringProperty firstNameProperty() {
        return firstName;
    }

    /**
     * Получение обёртки фамилии
     * @return обертка фамилии
     */
    public StringProperty lastNameProperty() {
        return lastName;
    }

    /**
     * Получение логина
     * @return логин
     */
    public String getLogin() {
        return login.get();
    }

    /**
     * Установка логина пользователю
     * @param login логин
     */
    public void setLogin(String login) {
        this.login.set(login);
    }

    /**
     * Установка пароля пользователю
     * @param password пароль
     */
    public void setPassword(String password) {
        this.password.set(password);
    }

    /**
     * Установка имени пользователю
     * @param firstName имя
     */
    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    /**
     * Установка фамилии пользователю
     * @param lastName фамилия
     */
    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    /**
     * Получение роли пользователя
     * @return роль
     */
    public String getRole() {
        return role.get();
    }

    /**
     * Преобразование объекта класса в строку
     * @return строка пользователя
     */
    @Override
    public String toString() {
        return "User{" +
                "login=" + login +
                ", password=" + password +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", role=" + role +
                ", status=" + status +
                '}';
    }

    /**
     * Преобразование объекта класса в json-строку для отправки на сервер
     * @return json-строка
     */
    @Override
    public String toJson() {
        Map<String, String> map = new HashMap<>();
        map.put("login", login.getValue());
        map.put("password", password.getValue());
        map.put("firstName", firstName.getValue());
        map.put("lastName", lastName.getValue());
        map.put("role", role.getValue());
        map.put("status", status.getValue());
        Gson gson = new Gson();
        return gson.toJson(map);
    }
}
