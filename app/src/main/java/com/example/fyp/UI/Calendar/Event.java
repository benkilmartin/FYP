package com.example.fyp.UI.Calendar;

import java.time.LocalDate;
import java.util.ArrayList;

public class Event {
    public static ArrayList<Event> eventsList = new ArrayList<>();

    public static ArrayList<Event> eventsForDate(LocalDate dates) {
        ArrayList<Event> events = new ArrayList<>();

        for (Event event : eventsList) {
            if (event.getDates().equals(dates))
                events.add(event);
        }

        return events;
    }


    private String subject;
    private int duration;
    private String date;
    private String time;
    private LocalDate dates;

    public Event(String subject, int duration, String date, String time, LocalDate dates) {
        this.subject = subject;
        this.duration = duration;
        this.date = date;
        this.time = time;
        this.dates = dates;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String name) {this.subject = subject;}

    public int getDuration() {return duration;}

    public void setDuration(int topic) {this.duration = duration;}

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

    public LocalDate getDates()
    {
        return dates;
    }

    public void setDates(LocalDate dates) {
        this.dates = dates;
    }

}
