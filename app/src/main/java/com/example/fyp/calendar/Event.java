package com.example.fyp.calendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event {
    public static ArrayList<Event> eventsList = new ArrayList<>();

    public static ArrayList<Event> eventsForDate(LocalDate date) {
        ArrayList<Event> events = new ArrayList<>();

        for (Event event : eventsList) {
            if (event.getDate().equals(date))
                events.add(event);
        }

        return events;
    }


    private String subject;
    private String topic;
    private String date;
    private LocalTime time;

    public Event(String subject, String topic, String date) {
        this.topic = topic;
        this.subject = subject;
        this.date = date;
        this.time = time;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String name) {this.subject = subject;}

    public String getTopic() {return topic;}

    public void setTopic(String topic) {this.topic = topic;}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {this.date = date;}

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

}
