package com.github.zeuschops.AppointmentManager.ERD;

import java.sql.Date;
import java.sql.Timestamp;

public class Appointment {
    private int appointmentId;
    private int customerId;
    private int userId; //I'm not sure what this is tied to, so this is the creator's UserID for now..
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String URL; //I'm not sure why this exists, so I just leave it as an empty String..
    private Timestamp start;
    private Timestamp end;
    private Date createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;

    public Appointment(int appointmentId, int customerId, int userId, String title, String description,
                       String location, String contact, String type, String URL, Timestamp start, Timestamp end,
                       Date createDate, String createdBy, Timestamp lastUpdate, String lastUpdateBy) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.URL = URL;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getContact() {
        return contact;
    }

    public String getType() {
        return type;
    }

    public String getURL() {
        return URL;
    }

    public Timestamp getStartDate() {
        return start;
    }

    public Timestamp getEndDate() {
        return end;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(Timestamp start) {
        this.start = start;
    }

    public void setEndDate(Timestamp end) {
        this.end = end;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setType(String type) {
        this.type = type;
    }
}
