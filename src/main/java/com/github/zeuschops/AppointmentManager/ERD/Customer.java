package com.github.zeuschops.AppointmentManager.ERD;

import java.sql.Date;
import java.sql.Timestamp;

public class Customer {
    private int customerId;
    private String customerName;
    private int addressId;
    private int active;
    private Date createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;

    public Customer(int customerId, String customerName, int addressId, int active, Date createDate, String createdBy, Timestamp lastUpdate, String lastUpdateBy) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.addressId = addressId;
        this.active = active;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getAddressId() {
        return addressId;
    }

    public int getActive() {
        return active;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
}
