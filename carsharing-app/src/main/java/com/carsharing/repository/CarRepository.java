package com.carsharing.repository;

import com.carsharing.model.Car;
import com.carsharing.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarRepository implements CrudRepository<Car, Long> {

    @Override
    public Car save(Car car) {
        String sql = "INSERT INTO cars (brand, model, license_plate, year, rate_per_hour, is_available) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, car.getBrand());
            stmt.setString(2, car.getModel());
            stmt.setString(3, car.getLicensePlate());
            stmt.setInt(4, car.getYear());
            stmt.setBigDecimal(5, car.getRatePerHour());
            stmt.setBoolean(6, car.getIsAvailable() != null ? car.getIsAvailable() : true);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) car.setId(rs.getLong("id"));
            return car;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения автомобиля: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Car> findById(Long id) {
        String sql = "SELECT * FROM cars WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapCar(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска автомобиля: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Car> findAll() {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM cars ORDER BY id";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) cars.add(mapCar(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения списка автомобилей: " + e.getMessage(), e);
        }
        return cars;
    }

    @Override
    public void update(Car car) {
        String sql = "UPDATE cars SET brand=?, model=?, license_plate=?, year=?, rate_per_hour=?, is_available=? WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, car.getBrand());
            stmt.setString(2, car.getModel());
            stmt.setString(3, car.getLicensePlate());
            stmt.setInt(4, car.getYear());
            stmt.setBigDecimal(5, car.getRatePerHour());
            stmt.setBoolean(6, car.getIsAvailable());
            stmt.setLong(7, car.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка обновления автомобиля: " + e.getMessage(), e);
        }
    }

    public void updateAvailability(Long carId, boolean isAvailable) {
        String sql = "UPDATE cars SET is_available = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, isAvailable);
            stmt.setLong(2, carId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка обновления статуса автомобиля: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM cars WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления автомобиля: " + e.getMessage(), e);
        }
    }

    private Car mapCar(ResultSet rs) throws SQLException {
        return new Car(
                rs.getLong("id"),
                rs.getString("brand"),
                rs.getString("model"),
                rs.getString("license_plate"),
                rs.getInt("year"),
                rs.getBigDecimal("rate_per_hour"),
                rs.getBoolean("is_available"));
    }
}
