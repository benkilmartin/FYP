package com.example.fyp.UI.Calendar;

public class Events {
    private String subject;
    private String tags;
    private int duration;
    private String date;
    private String time;
    private String id;
    private Boolean completed;

    public Events() {
    }

    public Events(String subject, String tags, int duration, String date, Boolean completed, String time, String id) {
        this.subject = subject;
        this.duration = duration;
        this.date = date;
        this.time = time;
        this.tags = tags;
        this.id = id;
        this.completed = completed;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {this.subject = subject;}

    public int getDuration() {
        return duration;}

    public void setDuration(int duration) {this.duration = duration;}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {this.date = date; }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
