package com.sedlex.object;

import java.util.ArrayList;

public class Law {

    private int id;
    private String lastUpdate;
    private String title;
    private String summary;
    private String progression;
    private ArrayList<Category> categories;
    private Stamp stamp;

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
                '}';
    }
}

