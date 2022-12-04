package com.github.zeuschops.AppointmentManager.ERD;

import java.sql.Date;
import java.sql.Timestamp;

public class User {
    public int userId;
    public String userName;
    public String password;
    public int active;
    public Date createDate;
    public String createdBy;
    public Timestamp lastUpdate;
    public String lastUpdateBy;
}
