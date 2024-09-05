package com.github.shadeslayer1467.ui.activitytracker.models;

import java.io.Serializable;

public class EventSessionModel implements Serializable {
    private int sessionId;
    private int eventId;
    private String startTime;
    private String endTime;
    private int duration;
    private boolean isDeleted;

    public EventSessionModel(int sessionId, int eventId, String startTime, String endTime, int duration, boolean deleted) {
        this.sessionId = sessionId;
        this.eventId = eventId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.isDeleted = deleted;
    }
    public EventSessionModel() {
        // Initialize default values if necessary
        this.sessionId = 0;
        this.eventId = 0;
        this.startTime = "";
        this.endTime = "";
        this.duration = 0;
        this.isDeleted = false;  // Assume a new session is not deleted by default
    }
    // Getters and Setters
    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

}

