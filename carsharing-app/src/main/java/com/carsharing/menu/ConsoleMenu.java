package com.carsharing.menu;

import com.carsharing.model.Car;
import com.carsharing.model.Rental;
import com.carsharing.model.RentalStatus;
import com.carsharing.model.User;
import com.carsharing.service.CarService;
import com.carsharing.service.RentalService;
import com.carsharing.service.StatisticsService;
import com.carsharing.service.UserService;
import com.carsharing.util.CsvExporter;
import com.carsharing.util.DataExporter;
import com.carsharing.util.ExcelExporter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Scanner;

public class ConsoleMenu {
    private final UserService userService;
    private final CarService carService;
    private final RentalService rentalService;
    private final StatisticsService statisticsService;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleMenu(UserService userService, CarService carService, RentalService rentalService, StatisticsService statisticsService) {
        this.userService = userService;
        this.carService = carService;
        this.rentalService = rentalService;
        this.statisticsService = statisticsService;
    }

    public void start() {
        while (true) {
            printMainMenu();
            int choice = readInt("Выберите действие: ");
            try {
                switch (choice) {
                    case 1 -> handleUserMenu();
                    case 2 -> handleCarMenu();
                    case 3 -> handleRentalCrudMenu();
                    case 4 -> handleSearchMenu();
                    case 5 -> handleFilterMenu();
                    case 6 -> statisticsService.printStatistics();
                    case 7 -> handleExportMenu();
                    case 8 -> printAllTables();
                    case 0 -> { System.out.println("Выход..."); return; }
                    default -> System.out.println("Неверный пункт меню.");
                }
            } catch (Exception e) {
                System.err.println("\n[ОШИБКА]: " + e.getMessage() + "\n");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("========== СИСТЕМА КАРШЕРИНГА ==========");
        System.out.println("1. Пользователи");
        System.out.println("2. Автомобили");
        System.out.println("3. Аренда (CRUD)");
        System.out.println("4. Поиск аренды");
        System.out.println("5. Фильтрация и сортировка");
        System.out.println("6. Статистика");
        System.out.println("7. Экспорт данных (Excel / CSV)");
        System.out.println("8. Вывести все таблицы БД");
        System.out.println("0. Выход");
    }

    private void handleUserMenu() {
        System.out.println("\n--- ПОЛЬЗОВАТЕЛИ ---");
        System.out.println("1. Список");
        System.out.println("2. Добавить");
        int sub = readInt("Выбор: ");
        if (sub == 1) {
            userService.getAllUsers().forEach(System.out::println);
        } else if (sub == 2) {
            System.out.print("ФИО: "); String name = scanner.nextLine();
            System.out.print("Email: "); String email = scanner.nextLine();
            System.out.print("Телефон: "); String phone = scanner.nextLine();
            System.out.print("Номер в/у: "); String license = scanner.nextLine();
            User user = userService.createUser(new User(null, name, email, phone, license, null));
            System.out.println("Создан: " + user);
        }
    }

    private void handleCarMenu() {
        System.out.println("\n--- АВТОМОБИЛИ ---");
        System.out.println("1. Список");
        System.out.println("2. Добавить");
        int sub = readInt("Выбор: ");
        if (sub == 1) {
            carService.getAllCars().forEach(System.out::println);
        } else if (sub == 2) {
            System.out.print("Марка: "); String brand = scanner.nextLine();
            System.out.print("Модель: "); String model = scanner.nextLine();
            System.out.print("Госномер: "); String plate = scanner.nextLine();
            int year = readInt("Год выпуска: ");
            System.out.print("Тариф в час: "); BigDecimal rate = new BigDecimal(scanner.nextLine());
            Car car = carService.createCar(new Car(null, brand, model, plate, year, rate, true));
            System.out.println("Добавлен: " + car);
        }
    }

    private void handleRentalCrudMenu() {
        System.out.println("\n--- АРЕНДА (CRUD) ---");
        System.out.println("1. Создать");
        System.out.println("2. Активировать");
        System.out.println("3. Завершить");
        System.out.println("4. Отменить");
        System.out.println("5. Показать все");
        System.out.println("6. Найти по ID");
        System.out.println("7. Удалить");
        int sub = readInt("Выбор: ");
        switch (sub) {
            case 1 -> {
                long uId = readLong("ID пользователя: ");
                long cId = readLong("ID авто: ");
                Rental r = rentalService.createRental(uId, cId, Timestamp.valueOf(LocalDateTime.now()));
                System.out.println("Создана: " + r);
            }
            case 2 -> {
                long id = readLong("ID аренды: ");
                rentalService.startRental(id);
                System.out.println("Аренда ACTIVE.");
            }
            case 3 -> {
                long id = readLong("ID аренды: ");
                rentalService.completeRental(id, Timestamp.valueOf(LocalDateTime.now()));
                System.out.println("Аренда завершена.");
            }
            case 4 -> {
                long id = readLong("ID аренды: ");
                rentalService.cancelRental(id);
                System.out.println("Аренда отменена.");
            }
            case 5 -> rentalService.getAllRentals().forEach(System.out::println);
            case 6 -> {
                long id = readLong("ID аренды: ");
                System.out.println(rentalService.getRentalById(id));
            }
            case 7 -> {
                long id = readLong("ID аренды: ");
                rentalService.deleteRental(id);
                System.out.println("Удалено.");
            }
        }
    }

    private void handleSearchMenu() {
        System.out.println("\n--- ПОИСК ---");
        System.out.println("1. По ID пользователя");
        System.out.println("2. По ID авто");
        int choice = readInt("Выбор: ");
        if (choice == 1) {
            rentalService.searchByUserId(readLong("ID пользователя: ")).forEach(System.out::println);
        } else if (choice == 2) {
            rentalService.searchByCarId(readLong("ID автомобиля: ")).forEach(System.out::println);
        }
    }

    private void handleFilterMenu() {
        System.out.println("\n--- ФИЛЬТР / СОРТИРОВКА ---");
        System.out.println("1. По статусу");
        System.out.println("2. По дате начала");
        System.out.println("3. По стоимости (убывание)");
        int choice = readInt("Выбор: ");
        if (choice == 1) {
            System.out.print("Статус (CREATED/ACTIVE/COMPLETED/CANCELLED): ");
            String s = scanner.nextLine().toUpperCase();
            rentalService.filterByStatus(RentalStatus.valueOf(s)).forEach(System.out::println);
        } else if (choice == 2) {
            rentalService.sortByStartTime().forEach(System.out::println);
        } else if (choice == 3) {
            rentalService.sortByTotalCost().forEach(System.out::println);
        }
    }

    private void handleExportMenu() {
        System.out.println("\n--- ЭКСПОРТ ---");
        System.out.println("1. Excel (.xlsx)");
        System.out.println("2. CSV (.csv)");
        int choice = readInt("Выбор: ");
        DataExporter exporter;
        String fileName;
        if (choice == 1) { exporter = new ExcelExporter(); fileName = "rentals_export.xlsx"; }
        else { exporter = new CsvExporter(); fileName = "rentals_export.csv"; }
        try {
            exporter.export(rentalService.getAllRentals(), fileName);
            System.out.println("Экспортировано в " + fileName);
        } catch (Exception e) {
            System.err.println("Ошибка экспорта: " + e.getMessage());
        }
    }

    private void printAllTables() {
        System.out.println("\n=== ПОЛЬЗОВАТЕЛИ ===");
        userService.getAllUsers().forEach(System.out::println);
        System.out.println("\n=== АВТОМОБИЛИ ===");
        carService.getAllCars().forEach(System.out::println);
        System.out.println("\n=== АРЕНДЫ ===");
        rentalService.getAllRentals().forEach(System.out::println);
    }

    private int readInt(String prompt) {
        while (true) {
            try { System.out.print(prompt); return Integer.parseInt(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Нужно целое число."); }
        }
    }

    private long readLong(String prompt) {
        while (true) {
            try { System.out.print(prompt); return Long.parseLong(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Нужно число."); }
        }
    }
}
