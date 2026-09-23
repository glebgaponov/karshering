package com.carsharing.repository;

import com.carsharing.model.Rental;
import com.carsharing.model.RentalStatus;
import com.carsharing.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RentalRepository implements CrudRepository<Rental, Long> {

    @Override
    public Rental save(Rental rental) {
        String sql = "INSERT INTO rentals (user_id, car_id, start_time, end_time, total_cost, status) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, rental.getUserId());
            stmt.setLong(2, rental.getCarId());
            stmt.setTimestamp(3, rental.getStartTime());
            stmt.setTimestamp(4, rental.getEndTime());
            stmt.setBigDecimal(5, rental.getTotalCost());
            stmt.setString(6, rental.getStatus().name());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) rental.setId(rs.getLong("id"));
            return rental;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения аренды: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Rental> findById(Long id) {
        String sql = "SELECT * FROM rentals WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapRental(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска аренды: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Rental> findAll() {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT * FROM rentals ORDER BY id";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) rentals.add(mapRental(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения списка аренд: " + e.getMessage(), e);
        }
        return rentals;
    }

    @Override
    public void update(Rental rental) {
        String sql = "UPDATE rentals SET user_id=?, car_id=?, start_time=?, end_time=?, total_cost=?, status=? WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, rental.getUserId());
            stmt.setLong(2, rental.getCarId());
            stmt.setTimestamp(3, rental.getStartTime());
            stmt.setTimestamp(4, rental.getEndTime());
            stmt.setBigDecimal(5, rental.getTotalCost());
            stmt.setString(6, rental.getStatus().name());
            stmt.setLong(7, rental.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка обновления аренды: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM rentals WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления аренды: " + e.getMessage(), e);
        }
    }

    private Rental mapRental(ResultSet rs) throws SQLException {
        return new Rental(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getLong("car_id"),
                rs.getTimestamp("start_time"),
                rs.getTimestamp("end_time"),
                rs.getBigDecimal("total_cost"),
                RentalStatus.valueOf(rs.getString("status")));
    }
}
