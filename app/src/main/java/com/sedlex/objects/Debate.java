package com.sedlex.objects;

public class Debate {

    private int id;
    private String deputyName;
    private Stamp stamp;
    private String text;
    private String size = "UNSET";

    public int getId() {
        return id;
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
                ", deputyName='" + deputyName + '\'' +
                ", stamp=" + stamp +
                ", text='" + text + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}
