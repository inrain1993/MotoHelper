package ru.motohelper.motohelper;


public class User {
    String login;
    String password;
    String firstName;
    String secondName;
    String phone;

    public User() {

    }

    public User(String login, String password, String firstName, String secondName, String phone) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.secondName = secondName;
        this.phone = phone;
    }

    //Setters

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    //Getters

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getPhone() {
        return phone;
    }

    public boolean userValid() {
        if (login != "" && password != "" && firstName != "" && secondName != "" && phone != "")
            return true;
        else return false;
    }
}
