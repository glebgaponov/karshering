package com.carsharing.model;

import java.math.BigDecimal;

public class Car {
    private Long id;
    private String brand;
    private String model;
    private String licensePlate;
    private Integer year;
    private BigDecimal ratePerHour;
    private Boolean isAvailable;

    public Car() {}

    public Car(Long id, String brand, String model, String licensePlate, Integer year, BigDecimal ratePerHour, Boolean isAvailable) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.licensePlate = licensePlate;
        this.year = year;
        this.ratePerHour = ratePerHour;
        this.isAvailable = isAvailable;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    public BigDecimal getRatePerHour() { return ratePerHour; }
    public void setRatePerHour(BigDecimal ratePerHour) { this.ratePerHour = ratePerHour; }
    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean available) { isAvailable = available; }

    @Override
    public String toString() {
        return String.format("Car{id=%d, brand='%s', model='%s', plate='%s', rate=%s/ч, available=%b}",
                id, brand, model, licensePlate, ratePerHour, isAvailable);
    }
}
