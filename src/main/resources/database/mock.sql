USE scan2class;
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