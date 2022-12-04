package com.github.zeuschops.AppointmentManager.Backend;

public class FormattedCalendarAppointment {
    private String title;
    private String date;

    public FormattedCalendarAppointment(String title, String date) {
        this.title = title;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String toString() {
        return title + " " + date;
    }
}
