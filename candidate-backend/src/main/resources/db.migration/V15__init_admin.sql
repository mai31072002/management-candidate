-- Enable UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ========================
-- 1. ROLE
-- ========================
INSERT INTO dtb_role (id, role_name)
VALUES
    (uuid_generate_v4(), 'ADMIN'),
    (uuid_generate_v4(), 'USER');

-- ========================
-- 2. LEVER
-- ========================
INSERT INTO dtb_lever (id, lever_name, description)
VALUES
    (uuid_generate_v4(), 1, 'Level 1');

-- ========================
-- 3. POSITION
-- ========================
INSERT INTO dtb_position (id, position_name, description, lever_id)
VALUES
    (
        uuid_generate_v4(),
        'Administrator',
        'System Admin',
        (SELECT id FROM dtb_lever LIMIT 1)
    );

-- ========================
-- 4. DEPARTMENT
-- ========================
INSERT INTO dtb_department (id, department_name, description)
VALUES
    (uuid_generate_v4(), 'IT', 'IT Department');

-- ========================
-- 5. EMPLOYEE
-- ========================
INSERT INTO dtb_employees (
    employee_id,
    first_name,
    last_name,
    full_name,
    email,
    phone,
    status,
    start_date,
    position_id,
    department_id,
    base_salary,
    allowance,
    employees_code
)
VALUES (
    uuid_generate_v4(),
    'Admin',
    'System',
    'Admin System',
    'admin@gmail.com',
    '0123456789',
    1,
    CURRENT_DATE,
    (SELECT id FROM dtb_position LIMIT 1),
    (SELECT id FROM dtb_department LIMIT 1),
    1000,
    0,
    'EMP001'
);

-- ========================
-- 6. USER
-- ========================
INSERT INTO dtb_user (
    user_id,
    username,
    password,
    email,
    employee_id
)
VALUES (
    uuid_generate_v4(),
    'admin',
    '$2a$10$Dow1u6p7RZcQ7dR0Zr6g1uR8f7Kp9bKXWQzQ7K8uKQyZxX8u7rK6a', -- password: admin123
    'admin@gmail.com',
    (SELECT employee_id FROM dtb_employees LIMIT 1)
);

-- ========================
-- 7. USER ROLE
-- ========================
INSERT INTO dtb_user_role (user_id, role_id)
VALUES (
    (SELECT user_id FROM dtb_user WHERE username = 'admin'),
    (SELECT id FROM dtb_role WHERE role_name = 'ADMIN')
);