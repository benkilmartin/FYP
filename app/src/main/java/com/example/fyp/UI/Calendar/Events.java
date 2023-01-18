package com.example.fyp.UI.Calendar;

public class Events {

    private String subject;
    private int duration;
    private String date;
    private String time;

    public Events() {
    }

    public Events(String subject, int duration, String date, String time) {
        this.subject = subject;
        this.duration = duration;
        this.date = date;
        this.time = time;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String name) {this.subject = subject;}

    public int getDuration() {
        return duration;}

    public void setDuration(int duration) {this.duration = duration;}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {this.date = date;}

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
