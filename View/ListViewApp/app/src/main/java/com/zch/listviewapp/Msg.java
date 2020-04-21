package com.zch.listviewapp;

public class Msg {

    private int profile;
    private String nickname;
    private String content;
    private boolean isLike;


    public Msg(int profile, String nickname, String content, boolean isLike) {
        this.profile=profile;
        this.nickname=nickname;
        this.content=content;
        this.isLike=isLike;
    }

    public int getProfile() {
        return profile;
    }

    public void setProfile(int profile) {
        this.profile=profile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content=content;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname=nickname;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike=like;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "profile=" + profile +
                ", nickname='" + nickname + '\'' +
                ", content='" + content + '\'' +
                ", isLike=" + isLike +
                '}';
    }
}
