package com.github.zeuschops.AppointmentManager.Backend;

public class FormattedAppointment {
    private int userId;
    private int appointmentId;
    private String type;
    private String customerName;
    private String length;
    private String time;

    public FormattedAppointment(int userId, int appointmentId, String type, String customerName, String length, String time) {
        this.userId = userId;
        this.appointmentId = appointmentId;
        this.type = type;
        this.customerName = customerName;
        this.length = length;
        this.time = time;
    }

    public int getUserId() {
        return userId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public String getType() {
        return type;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getLength() {
        return length;
    }

    public String getTime() {
        return time;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
