package com.sedlex.objects;

public class Debate {

    private int id;
    private boolean isSeparator = false;
    private int debatesCount = 0;
    private String deputyName;
    private Stamp stamp;
    private String text;
    private String size = "UNSET";

    // CONSTRUCTOR BY DEFAULT
    public Debate() {
    }

    // CONSTRUCTOR FOR SEPARATOR DEBATES
    public Debate(boolean isSeparator, int debatesCount, int id) {
        this.isSeparator = isSeparator;
        this.debatesCount = debatesCount;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isSeparator() {
        return isSeparator;
    }

    public void setSeparator(boolean isSeparator) {
        this.isSeparator = isSeparator;
    }

    public int getDebatesCount() {
        return debatesCount;
    }

    public void setDebatesCount(int debatesCount) {
        this.debatesCount = debatesCount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeputyName() {
        return deputyName;
    }

    public void setDeputyName(String deputyName) {
        this.deputyName = deputyName;
    }

    public Stamp getStamp() {
        return stamp;
    }

    public void setStamp(Stamp stamp) {
        this.stamp = stamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Debate{" +
                "id=" + id +
                ", isSeparator=" + isSeparator +
                ", debatesCount=" + debatesCount +
                ", deputyName='" + deputyName + '\'' +
                ", stamp=" + stamp +
                ", text='" + text + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}
