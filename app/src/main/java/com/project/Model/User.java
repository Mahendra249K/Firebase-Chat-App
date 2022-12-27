package com.project.Model;

import java.io.Serializable;

public class User {

    String id;
    String imageUrl;
    String username;
    String key;

    public User(String id, String imageUrl, String username) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.username = username;
    }

    public User() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
