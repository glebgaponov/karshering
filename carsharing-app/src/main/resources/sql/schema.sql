DROP TABLE IF EXISTS rentals;
DROP TABLE IF EXISTS cars;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    driver_license_number VARCHAR(30) UNIQUE NOT NULL,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cars (
    id BIGSERIAL PRIMARY KEY,
    brand VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    license_plate VARCHAR(20) UNIQUE NOT NULL,
    year INT CHECK (year >= 1990),
    rate_per_hour NUMERIC(10, 2) NOT NULL CHECK (rate_per_hour > 0),
    is_available BOOLEAN DEFAULT TRUE
);

CREATE TABLE rentals (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    car_id BIGINT NOT NULL REFERENCES cars(id) ON DELETE CASCADE,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    total_cost NUMERIC(10, 2),
    status VARCHAR(20) NOT NULL CHECK (status IN ('CREATED', 'ACTIVE', 'COMPLETED', 'CANCELLED'))
);

INSERT INTO users (full_name, email, phone, driver_license_number) VALUES
('Алексей Иванов', 'alex.ivanov@mail.ru', '+79991112233', 'DL-100100'),
('Мария Петрова', 'maria.petrova@yandex.ru', '+79992223344', 'DL-100200'),
('Сергей Смирнов', 's.smirnov@gmail.com', '+79993334455', 'DL-100300'),
('Елена Сидорова', 'e.sidorova@inbox.ru', '+79994445566', 'DL-100400'),
('Дмитрий Кузнецов', 'd.kuznetsov@rambler.ru', '+79995556677', 'DL-100500');

INSERT INTO cars (brand, model, license_plate, year, rate_per_hour, is_available) VALUES
('Kia', 'Rio', 'А123АА777', 2021, 400.00, true),
('Hyundai', 'Solaris', 'В456ВВ777', 2022, 450.00, true),
('Volkswagen', 'Polo', 'Е789ЕЕ777', 2020, 420.00, false),
('Skoda', 'Rapid', 'К012КК777', 2021, 430.00, true),
('BMW', '3 Series', 'М345ММ777', 2023, 1200.00, true),
('Mercedes-Benz', 'C-Class', 'Н678НН777', 2022, 1300.00, true),
('Audi', 'A4', 'О901ОО777', 2022, 1250.00, false),
('Toyota', 'Camry', 'Р234РР777', 2021, 800.00, true),
('Nissan', 'Qashqai', 'С567СС777', 2020, 650.00, true),
('Havall', 'Jolion', 'Т890ТТ777', 2023, 550.00, true);

INSERT INTO rentals (user_id, car_id, start_time, end_time, total_cost, status) VALUES
(1, 1, '2026-09-01 10:00:00', '2026-09-01 12:00:00', 800.00, 'COMPLETED'),
(1, 2, '2026-09-02 14:00:00', '2026-09-02 17:00:00', 1350.00, 'COMPLETED'),
(2, 3, '2026-09-15 08:00:00', NULL, NULL, 'ACTIVE'),
(2, 4, '2026-09-03 09:00:00', '2026-09-03 10:00:00', 430.00, 'COMPLETED'),
(3, 5, '2026-09-04 18:00:00', '2026-09-04 20:00:00', 2400.00, 'COMPLETED'),
(3, 6, '2026-09-05 12:00:00', '2026-09-05 13:00:00', 1300.00, 'COMPLETED'),
(4, 7, '2026-09-16 11:00:00', NULL, NULL, 'ACTIVE'),
(4, 8, '2026-09-06 15:00:00', '2026-09-06 18:00:00', 2400.00, 'COMPLETED'),
(5, 9, '2026-09-07 10:00:00', '2026-09-07 11:00:00', 650.00, 'CANCELLED'),
(5, 10, '2026-09-08 13:00:00', '2026-09-08 16:00:00', 1650.00, 'COMPLETED'),
(1, 5, '2026-09-09 19:00:00', '2026-09-09 22:00:00', 3600.00, 'COMPLETED'),
(2, 1, '2026-09-10 07:00:00', '2026-09-10 09:00:00', 800.00, 'COMPLETED'),
(3, 2, '2026-09-11 16:00:00', '2026-09-11 18:00:00', 900.00, 'COMPLETED'),
(4, 4, '2026-09-12 12:00:00', '2026-09-12 13:00:00', 430.00, 'CANCELLED'),
(5, 8, '2026-09-20 10:00:00', NULL, NULL, 'CREATED');
