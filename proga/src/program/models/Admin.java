package program.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Admin {

    private StringProperty login;
    private StringProperty password;

    public Admin(String login, String password) {
        this.login = new SimpleStringProperty(login);
        this.password = new SimpleStringProperty(password);
    }

    public StringProperty loginProperty() {
        return login;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public String getLogin() {
        return login.get();
    }

    public String getPassword() {
        return password.get();
    }

    //потом вот этого быть не должно, когда уже будет бд

    public void setLogin(String login) {
        this.login.set(login);
    }

    public void setPassword(String password) {
        this.password.set(password);
    }
}
