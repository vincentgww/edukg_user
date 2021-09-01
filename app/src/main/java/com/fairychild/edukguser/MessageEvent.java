package com.fairychild.edukguser;

public class MessageEvent {
    private String message;

    public MessageEvent(String message) {
        setMessage(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
