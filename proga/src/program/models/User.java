package program.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {

    private StringProperty login;
    private StringProperty password;
    private StringProperty firstName;
    private StringProperty lastName;

    //public User(){}

    public User(String login, String password, String firstName, String lastName) {
        this.login = new SimpleStringProperty(login);
        this.password = new SimpleStringProperty(password);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
    }

    public String login() {
        return login.get();
    }

    public String password() {
        return password.get();
    }

    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
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
}
