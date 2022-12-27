package com.project.Model;

public class ChatModel {

    String sender;
    String reciver;
    String message;
    String key;

    public ChatModel() {
    }

    public ChatModel(String sender, String reciver, String message) {
        this.sender = sender;
        this.reciver = reciver;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
