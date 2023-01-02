package com.example.fyp.calendar;

import java.time.LocalDate;
import java.util.ArrayList;

public class Events {

    private String subject;
    private String duration;
    private String date;
    private String time;


    public Events(String subject, String duration, String date, String time) {
        this.subject = subject;
        this.duration = duration;
        this.date = date;
        this.time = time;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String name) {this.subject = subject;}

    public String getDuration() {return duration;}

    public void setDuration(String topic) {this.duration = duration;}

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
