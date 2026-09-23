package com.carsharing.service;

import com.carsharing.exception.BusinessException;
import com.carsharing.exception.EntityNotFoundException;
import com.carsharing.exception.ValidationException;
import com.carsharing.model.Car;
import com.carsharing.model.Rental;
import com.carsharing.model.RentalStatus;
import com.carsharing.repository.CarRepository;
import com.carsharing.repository.RentalRepository;
import com.carsharing.repository.UserRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RentalService {
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;

    public RentalService(RentalRepository rentalRepository, UserRepository userRepository, CarRepository carRepository) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
        this.carRepository = carRepository;
    }

    public Rental createRental(Long userId, Long carId, Timestamp startTime) {
        userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Несуществующий пользователь ID " + userId));
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new BusinessException("Несуществующий автомобиль ID " + carId));
        if (!Boolean.TRUE.equals(car.getIsAvailable())) {
            throw new BusinessException("Автомобиль ID " + carId + " недоступен для аренды.");
        }
        if (startTime.before(Timestamp.valueOf(LocalDateTime.now().minusMinutes(5)))) {
            throw new BusinessException("Время начала аренды не может быть в прошлом.");
        }
        Rental rental = new Rental(null, userId, carId, startTime, null, null, RentalStatus.CREATED);
        return rentalRepository.save(rental);
    }

    public void startRental(Long rentalId) {
        Rental rental = getRentalById(rentalId);
        if (rental.getStatus() != RentalStatus.CREATED) {
            throw new BusinessException("Аренда может быть переведена в ACTIVE только из статуса CREATED.");
        }
        rental.setStatus(RentalStatus.ACTIVE);
        carRepository.updateAvailability(rental.getCarId(), false);
        rentalRepository.update(rental);
    }

    public void completeRental(Long rentalId, Timestamp endTime) {
        Rental rental = getRentalById(rentalId);
        if (rental.getStatus() != RentalStatus.ACTIVE) {
            throw new BusinessException("Нельзя завершить аренду, если она не в статусе ACTIVE.");
        }
        if (endTime.before(rental.getStartTime())) {
            throw new ValidationException("Время окончания не может быть раньше времени начала.");
        }
        Car car = carRepository.findById(rental.getCarId())
                .orElseThrow(() -> new EntityNotFoundException("Автомобиль не найден"));
        long durationMillis = endTime.getTime() - rental.getStartTime().getTime();
        double hours = Math.max(1.0, durationMillis / (1000.0 * 60 * 60));
        BigDecimal totalCost = car.getRatePerHour().multiply(BigDecimal.valueOf(hours)).setScale(2, RoundingMode.HALF_UP);
        rental.setEndTime(endTime);
        rental.setTotalCost(totalCost);
        rental.setStatus(RentalStatus.COMPLETED);
        carRepository.updateAvailability(car.getId(), true);
        rentalRepository.update(rental);
    }

    public void cancelRental(Long rentalId) {
        Rental rental = getRentalById(rentalId);
        if (rental.getStatus() == RentalStatus.COMPLETED) {
            throw new BusinessException("Нельзя отменить уже завершённую аренду.");
        }
        rental.setStatus(RentalStatus.CANCELLED);
        carRepository.updateAvailability(rental.getCarId(), true);
        rentalRepository.update(rental);
    }

    public Rental getRentalById(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Аренда с ID " + id + " не найдена."));
    }

    public List<Rental> getAllRentals() { return rentalRepository.findAll(); }

    public void deleteRental(Long id) {
        if (!rentalRepository.deleteById(id)) {
            throw new EntityNotFoundException("Не удалось удалить: аренда с ID " + id + " не найдена.");
        }
    }

    public List<Rental> searchByUserId(Long userId) {
        return rentalRepository.findAll().stream()
                .filter(r -> r.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Rental> searchByCarId(Long carId) {
        return rentalRepository.findAll().stream()
                .filter(r -> r.getCarId().equals(carId))
                .collect(Collectors.toList());
    }

    public List<Rental> filterByStatus(RentalStatus status) {
        return rentalRepository.findAll().stream()
                .filter(r -> r.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Rental> sortByStartTime() {
        return rentalRepository.findAll().stream()
                .sorted(Comparator.comparing(Rental::getStartTime))
                .collect(Collectors.toList());
    }

    public List<Rental> sortByTotalCost() {
        return rentalRepository.findAll().stream()
                .sorted(Comparator.comparing(
                        r -> r.getTotalCost() != null ? r.getTotalCost() : BigDecimal.ZERO,
                        Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }
}
