package com.sedlex.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Law {

    private int id;
    private String lastUpdate;
    private String title;
    private String summary;
    private String progression;
    private String content;
    private ArrayList<Category> categories;
    private Stamp stamp;
    private Date day_order;
    private boolean hasDebates;
    private List<String> debatesGroupList;
    private ArrayList<Article> articleList;
    private boolean isDummyLoadingView = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getProgression() {
        return progression;
    }

    public void setProgression(String progression) {
        this.progression = progression;
    }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public Stamp getStamp() {
        return stamp;
    }

    public void setStamp(Stamp stamp) {
        this.stamp = stamp;
    }

    public Date getDayOrder() {
        return day_order;
    }

    public void setDayOrder(Date day_order) {
        this.day_order = day_order;
    }

    public boolean getHasDebates() {return hasDebates; }

    public void setHasDebates(boolean hasDebates) { this.hasDebates = hasDebates; }

    public List<String> getDebatesGroupList() { return debatesGroupList; }

    public void setDebatesGroupList(List<String> debatesGroupList) { this.debatesGroupList = debatesGroupList; }

    public ArrayList<Article> getArticleList() { return articleList; }

    public void setArticleList(ArrayList<Article> articleList) { this.articleList = articleList; }

    public boolean isDummyLoadingView() {
        return isDummyLoadingView;
    }

    public void setDummyLoadingView(boolean isDummyLoadingView) {
        this.isDummyLoadingView = isDummyLoadingView;
    }


    @Override
    public String toString() {
        return "Law{" +
                "id=" + id +
                ", lastUpdate='" + lastUpdate + '\'' +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", progression='" + progression + '\'' +
                ", categories=" + categories +
                ", stamp=" + stamp +
                ", day_order=" + day_order +
                '}';
    }
}

