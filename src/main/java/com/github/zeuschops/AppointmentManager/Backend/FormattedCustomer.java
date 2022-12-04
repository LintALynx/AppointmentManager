package com.github.zeuschops.AppointmentManager.Backend;

public class FormattedCustomer {
    public String customerName;
    public String address;
    public int customerId;
    public int addressId;
    public String phoneNumber;

    public FormattedCustomer(String customerName, String address, String phoneNumber, int customerId, int addressId) {
        this.customerName = customerName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.customerId = customerId;
        this.addressId = addressId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getAddressId() {
        return addressId;
    }

    public int getCustomerId() {
        return customerId;
    }
}
