package com.carsharing.service;

import com.carsharing.exception.EntityNotFoundException;
import com.carsharing.exception.ValidationException;
import com.carsharing.model.Car;
import com.carsharing.repository.CarRepository;

import java.util.List;

public class CarService {
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car createCar(Car car) {
        if (car.getBrand() == null || car.getBrand().isEmpty()) {
            throw new ValidationException("Марка авто не может быть пустой.");
        }
        if (car.getRatePerHour() == null || car.getRatePerHour().doubleValue() <= 0) {
            throw new ValidationException("Тариф в час должен быть больше 0.");
        }
        return carRepository.save(car);
    }

    public Car getCarById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Автомобиль с ID " + id + " не найден."));
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public void updateAvailability(Long carId, boolean isAvailable) {
        carRepository.updateAvailability(carId, isAvailable);
    }
}
