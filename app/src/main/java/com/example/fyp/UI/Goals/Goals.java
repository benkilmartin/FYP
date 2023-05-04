package com.example.fyp.UI.Goals;

public class Goals {

    String title, description, date, startDate, progress, id;


    public Goals() {
    }

    public Goals(String title, String description, String date, String startDate, String progress, String id) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.startDate = startDate;
        this.progress = progress;
        this.id = id;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
