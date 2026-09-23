package com.carsharing;

import com.carsharing.menu.ConsoleMenu;
import com.carsharing.repository.CarRepository;
import com.carsharing.repository.RentalRepository;
import com.carsharing.repository.UserRepository;
import com.carsharing.service.CarService;
import com.carsharing.service.RentalService;
import com.carsharing.service.StatisticsService;
import com.carsharing.service.UserService;

public class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        CarRepository carRepository = new CarRepository();
        RentalRepository rentalRepository = new RentalRepository();

        UserService userService = new UserService(userRepository);
        CarService carService = new CarService(carRepository);
        RentalService rentalService = new RentalService(rentalRepository, userRepository, carRepository);
        StatisticsService statisticsService = new StatisticsService(userRepository, carRepository, rentalRepository);

        new ConsoleMenu(userService, carService, rentalService, statisticsService).start();
    }
}
