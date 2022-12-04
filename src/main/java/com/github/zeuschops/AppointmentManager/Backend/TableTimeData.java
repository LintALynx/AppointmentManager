package com.github.zeuschops.AppointmentManager.Backend;

public class TableTimeData {
    private String time;
    private String sunday;
    private String monday;
    private String tuesday;
    private String wednesday;
    private String thursday;
    private String friday;
    private String saturday;

    public TableTimeData(String time, String sunday, String monday, String tuesday, String wednesday, String thursday, String friday, String saturday) {
        this.time = time;
        this.sunday = sunday != null ? sunday : "";
        this.monday = monday != null ? monday : "";
        this.tuesday = tuesday != null ? tuesday : "";
        this.wednesday = wednesday != null ? wednesday : "";
        this.thursday = thursday != null ? thursday : "";
        this.friday = friday != null ? friday : "";
        this.saturday = saturday != null ? saturday : "";
    }

    public String getTime() {
        return time;
    }

    public String getSunday() {
        return sunday;
    }

    public String getMonday() {
        return monday;
    }

    public String getTuesday() {
        return tuesday;
    }

    public String getWednesday() {
        return wednesday;
    }

    public String getThursday() {
        return thursday;
    }

    public String getFriday() {
        return friday;
    }

    public String getSaturday() {
        return saturday;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setSunday(String sunday) {
        this.sunday = sunday;
    }

    public void setMonday(String monday) {
        this.monday = monday;
    }

    public void setTuesday(String tuesday) {
        this.tuesday = tuesday;
    }

    public void setWednesday(String wednesday) {
        this.wednesday = wednesday;
    }

    public void setThursday(String thursday) {
        this.thursday = thursday;
    }

    public void setFriday(String friday) {
        this.friday = friday;
    }

    public void setSaturday(String saturday) {
        this.saturday = saturday;
    }

    public String toString() {
        return "time: " + time + "; sunday: " + sunday + "; monday: " + monday + "; tuesday: " + tuesday + "; wednesday: " + wednesday + "; thursday: " + "; friday: " + friday + "; saturday" + saturday;
    }
}