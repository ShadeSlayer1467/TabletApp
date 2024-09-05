package com.github.shadeslayer1467.ui.hobbytracker.models;

import java.io.Serializable;

public class EventModel implements Serializable {
    private int eventId;
    private String eventName;
    private int categoryId;
    private String categoryName;
    private String createdAt;
    private String updatedAt;
    private long totalMS;

    public EventModel(int eventId, String eventName, int categoryId, String categoryName, String createdAt, String updatedAt, long totalMS) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.totalMS = totalMS;
    }

    // Getters and Setters
    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getTotalMS() { return totalMS; }

    public void setTotalMS(long totalMS) {this.totalMS = totalMS; }
}

