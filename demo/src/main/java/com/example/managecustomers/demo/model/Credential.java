package com.example.managecustomers.demo.model;

public class Credential {
    private String login_id;
    private String password;
    public String getlogin_id() {
        return login_id;
    }
    public void setlogin_id(String login_id) {
        this.login_id = login_id;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String toString() {
        return "Credential [login_id=" + login_id + ", password=" + password + "]";
    }
}
