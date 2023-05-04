package com.example.fyp.UI.Calendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class Event {
    public static ArrayList<Event> eventsList = new ArrayList<>();

    public static ArrayList<Event> eventsForDate(LocalDate dates) {
        ArrayList<Event> events = new ArrayList<>();
        for (Event event : eventsList) {
            if (event.getDates().equals(dates)) {
                events.add(event);
            }
        }

        return events;
    }


    private String subject;
    private String tags;
    private String date;
    private String time;
    private String id;
    private Boolean completed;
    private int duration;
    private LocalDate dates;


    public Event(String subject, String tags, int duration, String date, String time, Boolean completed, LocalDate dates, String id) {
        this.subject = subject;
        this.duration = duration;
        this.date = date;
        this.time = time;
        this.dates = dates;
        this.tags = tags;
        this.id = id;
        this.completed = completed;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int topic) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public LocalDate getDates() {
        return dates;
    }

    public void setDates(LocalDate dates) {
        this.dates = dates;
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
