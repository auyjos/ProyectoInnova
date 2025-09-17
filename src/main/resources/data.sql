-- Datos iniciales para testing
-- Spring Boot ejecutará este archivo automáticamente al iniciar la aplicación

-- Insertar usuario admin si no existe
INSERT INTO users (id, name, email, password, phone, role, is_active, created_at, updated_at) 
VALUES (1, 'Admin User', 'admin@restaurant.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '+1234567890', 'ADMIN', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- Insertar propietario de restaurante si no existe
INSERT INTO users (id, name, email, password, phone, role, is_active, created_at, updated_at)
VALUES (2, 'Restaurant Owner', 'owner@restaurant.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '+1234567891', 'RESTAURANT_OWNER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- Insertar cliente si no existe
INSERT INTO users (id, name, email, password, phone, role, is_active, created_at, updated_at)
VALUES (3, 'Customer User', 'customer@restaurant.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '+1234567892', 'CUSTOMER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- Insertar restaurante si no existe
INSERT INTO restaurants (id, name, address, phone, email, opening_time, closing_time, capacity, owner_id, is_active, created_at, updated_at)
VALUES (1, 'La Bella Vita', 'Calle Mayor 123', '+34912345678', 'info@labellavita.com', '08:00:00', '23:00:00', 100, 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- Insertar mesas si no existen
INSERT INTO restaurant_tables (id, table_number, capacity, restaurant_id, status, created_at, updated_at)
VALUES 
    (1, 1, 4, 1, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 2, 2, 1, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 3, 6, 1, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (4, 4, 8, 1, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- Resetear secuencias para que los próximos inserts usen IDs disponibles
SELECT setval('users_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM users), false);
SELECT setval('restaurants_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM restaurants), false);
SELECT setval('restaurant_tables_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM restaurant_tables), false);