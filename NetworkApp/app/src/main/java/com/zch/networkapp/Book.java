package com.zch.networkapp;

public class Book {

    private String title;
    private String author;
    private String content;

    public Book(String title, String author, String content) {
        this.title=title;
        this.author=author;
        this.content=content;
    }

    public Book() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author=author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content=content;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
