package com.carsharing.model;

import java.sql.Timestamp;

public class User {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String driverLicenseNumber;
    private Timestamp registrationDate;

    public User() {}

    public User(Long id, String fullName, String email, String phone, String driverLicenseNumber, Timestamp registrationDate) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.driverLicenseNumber = driverLicenseNumber;
        this.registrationDate = registrationDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getDriverLicenseNumber() { return driverLicenseNumber; }
    public void setDriverLicenseNumber(String driverLicenseNumber) { this.driverLicenseNumber = driverLicenseNumber; }
    public Timestamp getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(Timestamp registrationDate) { this.registrationDate = registrationDate; }

    @Override
    public String toString() {
        return String.format("User{id=%d, name='%s', email='%s', phone='%s', license='%s'}",
                id, fullName, email, phone, driverLicenseNumber);
    }
}
