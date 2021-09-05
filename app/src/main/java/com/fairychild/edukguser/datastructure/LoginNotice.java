package com.fairychild.edukguser.datastructure;

public class LoginNotice {

    String username;

    public LoginNotice(String message) {
        setMessage(message);
    }

    public String getMessage() {
        return username;
    }

    public void setMessage(String message) {
        this.username = message;
    }
}