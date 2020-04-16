package com.zch.restapp.bean;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String username;
    private String password;
    private int icon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username=username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password=password;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon=icon;
    }
}
