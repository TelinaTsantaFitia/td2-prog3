CREATE DATABASE mini_dish_db;

CREATE USER mini_dish_db_manager;

GRANT CONNECT ON DATABASE mini_dish_db TO mini_dish_db_manager;

GRANT USAGE, CREATE ON SCHEMA public TO mini_dish_db_manager;

GRANT SELECT, INSERT, UPDATE, DELETE
ON ALL TABLES IN SCHEMA public
TO mini_dish_db_manager;