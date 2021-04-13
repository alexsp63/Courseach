package program.models;

import com.google.gson.Gson;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.HashMap;
import java.util.Map;

public class User implements JSONSerialize{

    private StringProperty login;
    private StringProperty password;
    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty role;
    private StringProperty status;

    public User(){
        this(null, null, null, null);
    }

    public User(String login, String password, String firstName, String lastName) {
        this.login = new SimpleStringProperty(login);
        this.password = new SimpleStringProperty(password);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.role = new SimpleStringProperty("USER");
        this.status = new SimpleStringProperty("ACTIVE");
    }

    public User(String login, String password, String firstName, String lastName, String role, String status) {
        this.login = new SimpleStringProperty(login);
        this.password = new SimpleStringProperty(password);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.role = new SimpleStringProperty(role);
        this.status = new SimpleStringProperty(status);
    }


    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getPassword() {
        return password.get();
    }

    public String getStatus() {
        return status.get();
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public StringProperty loginProperty() {
        return login;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public String getLogin() {
        return login.get();
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getRole() {
        return role.get();
    }

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
