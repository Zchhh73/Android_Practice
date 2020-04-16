package com.zch.restapp.bean;

import java.io.Serializable;

public class Product implements Serializable {
    protected int id;
    protected String name;
    protected String label;
    protected String icon;
    protected String description;
    protected float price;
    protected Restaurant restaurant;

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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label=label;
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

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant=restaurant;
    }
}
