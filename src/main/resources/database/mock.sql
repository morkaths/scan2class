USE scan2class;

CREATE TABLE IF NOT EXISTS permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS roles_permissions (
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (permission_id) REFERENCES permissions(id)
);

-- Bảng permission
INSERT INTO permissions (code, name) VALUES
('user:view', 'View Users'),
('user:edit', 'Edit Users'),
('role:view', 'Edit Roles'),
('role:edit', 'Edit Roles'); 

-- Bảng role
INSERT INTO roles (code, name) VALUES
('ADMIN', 'Administrator'),
('USER', 'Regular User'),
('MANAGER', 'Manager'),
('STAFF', 'Staff');

-- Bảng trung gian role_permission
INSERT INTO roles_permissions (role_id, permission_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), -- Admin: all permissions
(2, 1),                         -- User: view user permission
(3, 1), (3, 3),                 -- Manager: view user, view role
(4, 1);                         -- Staff: view user

INSERT INTO roles (code, name) VALUES
('GUEST', 'Guest/Public Access'),
('SALES', 'Sales Representative'),
('HR', 'Human Resources Specialist'),
('ACCOUNTANT', 'Financial Accountant'),
('EDITOR', 'Content Editor'),
('AUDITOR', 'System Auditor'),
('DEVELOPER', 'System Developer'),
('SUPPORT', 'Technical Support Staff'),
('SUPERVISOR', 'Team Supervisor'),
('FINANCE', 'Finance Department');