package com.carsharing.service;

import com.carsharing.model.Rental;
import com.carsharing.model.RentalStatus;
import com.carsharing.repository.CarRepository;
import com.carsharing.repository.RentalRepository;
import com.carsharing.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;

public class StatisticsService {
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final RentalRepository rentalRepository;

    public StatisticsService(UserRepository userRepository, CarRepository carRepository, RentalRepository rentalRepository) {
        this.userRepository = userRepository;
        this.carRepository = carRepository;
        this.rentalRepository = rentalRepository;
    }

    public void printStatistics() {
        List<Rental> rentals = rentalRepository.findAll();
        long totalUsers = userRepository.findAll().size();
        long totalCars = carRepository.findAll().size();
        long totalRentals = rentals.size();
        long activeRentals = rentals.stream().filter(r -> r.getStatus() == RentalStatus.ACTIVE).count();
        long completedRentals = rentals.stream().filter(r -> r.getStatus() == RentalStatus.COMPLETED).count();
        BigDecimal totalRevenue = rentals.stream()
                .filter(r -> r.getStatus() == RentalStatus.COMPLETED && r.getTotalCost() != null)
                .map(Rental::getTotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("\n========== СТАТИСТИКА ==========");
        System.out.println("Пользователей: " + totalUsers);
        System.out.println("Автомобилей:   " + totalCars);
        System.out.println("Всего аренд:   " + totalRentals);
        System.out.println("Активных:      " + activeRentals);
        System.out.println("Завершённых:   " + completedRentals);
        System.out.println("Выручка:       " + totalRevenue + " руб.");
        System.out.println("================================\n");
    }
}
