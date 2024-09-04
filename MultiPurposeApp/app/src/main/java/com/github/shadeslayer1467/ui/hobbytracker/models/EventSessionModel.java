package com.github.shadeslayer1467.ui.hobbytracker.models;

public class EventSessionModel {
        private int sessionId;
        private int eventId;
        private String startTime;
        private String endTime;
        private int duration;

        public EventSessionModel(int sessionId, int eventId, String startTime, String endTime, int duration) {
            this.sessionId = sessionId;
            this.eventId = eventId;
            this.startTime = startTime;
            this.endTime = endTime;
            this.duration = duration;
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
    }

