package com.zch.restapp.bean;

import android.content.Intent;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order implements Serializable{

    public static class ProductVo implements Serializable {
        public Product product;
        public int count;
    }

    public Map<Product,Integer> productMap = new HashMap<>();

    private int id;
    private Date date;
    private Restaurant restaurant;
    private int count;
    private float price;

    private List<ProductVo> ps;

    //对productMap更新
    public void addProduct(Product product){
        Integer count=productMap.get(product);
        if(count == null || count == 0){
            count = 1;
        }else{
            count++;
        }
        productMap.put(product,count);
    }

    public void removeProduct(Product product){
        Integer count=productMap.get(product);
        if(count==null || count<=0){
            return;
        }
        count--;
        productMap.put(product,count);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date=date;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant=restaurant;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count=count;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price=price;
    }

    public List<ProductVo> getPs() {
        return ps;
    }

    public void setPs(List<ProductVo> ps) {
        this.ps=ps;
    }
}
