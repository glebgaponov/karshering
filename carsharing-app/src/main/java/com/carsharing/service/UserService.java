package com.carsharing.service;

import com.carsharing.exception.EntityNotFoundException;
import com.carsharing.exception.ValidationException;
import com.carsharing.model.User;
import com.carsharing.repository.UserRepository;

import java.util.List;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            throw new ValidationException("Имя пользователя не может быть пустым.");
        }
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Некорректный email.");
        }
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + id + " не найден."));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
