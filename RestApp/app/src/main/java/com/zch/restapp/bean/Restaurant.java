package com.zch.restapp.bean;

import java.io.Serializable;

public class Restaurant implements Serializable {
    private int id;
    private String name;
    private String icon;
    private String description;
    private float price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon=icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description=description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price=price;
    }
}
