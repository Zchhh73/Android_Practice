package com.zch.expandablelistview.bean;

import java.util.ArrayList;
import java.util.List;

public class Chapter {
    private int id;
    private String name;

    public static final String TABLE_NAME = "tb_chapter";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "name";


    private List<ChapterItem> children = new ArrayList<>();


    public Chapter() {
    }

    public Chapter(int id, String name) {
        this.id=id;
        this.name=name;
    }

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

    public List<ChapterItem> getChildren() {
        return children;
    }

    public void setChildren(List<ChapterItem> children) {
        this.children=children;
    }

    public void addChild(ChapterItem chapterItem){
        chapterItem.setPid(getId());
        children.add(chapterItem);
    }

    public void addChild(int cid,String cname){
        ChapterItem chapterItem = new ChapterItem(cid,cname);
        chapterItem.setPid(getId());
        children.add(chapterItem);
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", children=" + children +
                '}';
    }
}
