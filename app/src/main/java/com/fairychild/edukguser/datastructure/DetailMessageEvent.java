package com.fairychild.edukguser.datastructure;

public class DetailMessageEvent {
    private final String message;

    public DetailMessageEvent(String message) {
        this.message = message;
    }
    public String getMessage(){
        return message;
    }
}
