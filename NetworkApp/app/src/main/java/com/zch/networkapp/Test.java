package com.zch.networkapp;

public class Test {
    private int status;
    private String msg;
    private Book data;

    public Test(int status, String msg, Book data) {
        this.status=status;
        this.msg=msg;
        this.data=data;
    }

    public Test() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status=status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg=msg;
    }

    public Book getData() {
        return data;
    }

    public void setData(Book data) {
        this.data=data;
    }

    @Override
    public String toString() {
        return "Test{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
