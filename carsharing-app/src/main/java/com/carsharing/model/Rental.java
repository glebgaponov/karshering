package com.carsharing.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Rental {
    private Long id;
    private Long userId;
    private Long carId;
    private Timestamp startTime;
    private Timestamp endTime;
    private BigDecimal totalCost;
    private RentalStatus status;

    public Rental() {}

    public Rental(Long id, Long userId, Long carId, Timestamp startTime, Timestamp endTime, BigDecimal totalCost, RentalStatus status) {
        this.id = id;
        this.userId = userId;
        this.carId = carId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalCost = totalCost;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getCarId() { return carId; }
    public void setCarId(Long carId) { this.carId = carId; }
    public Timestamp getStartTime() { return startTime; }
    public void setStartTime(Timestamp startTime) { this.startTime = startTime; }
    public Timestamp getEndTime() { return endTime; }
    public void setEndTime(Timestamp endTime) { this.endTime = endTime; }
    public BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }
    public RentalStatus getStatus() { return status; }
    public void setStatus(RentalStatus status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("Rental{id=%d, userId=%d, carId=%d, start=%s, end=%s, cost=%s, status=%s}",
                id, userId, carId, startTime, endTime, totalCost != null ? totalCost : "N/A", status);
    }
}
