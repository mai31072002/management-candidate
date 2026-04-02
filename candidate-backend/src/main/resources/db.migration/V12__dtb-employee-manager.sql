-- Bảng trung gian User-Role
CREATE TABLE IF NOT EXISTS dtb_employee_manager (
    id UUID NOT NULL,
    employee_id UUID NOT NULL,
    manager_id UUID NOT NULL,
    version         int                 NOT NULL DEFAULT 0,
    create_at       timestamp(0)        DEFAULT CURRENT_TIMESTAMP,
    update_at       timestamp(0)        DEFAULT CURRENT_TIMESTAMP,
    is_deleted          boolean      NOT NULL DEFAULT false,
    PRIMARY KEY (employee_id, manager_id),
    CONSTRAINT fk_employee FOREIGN KEY (employee_id) REFERENCES dtb_employee(employee_id) ON DELETE CASCADE,
    CONSTRAINT fk_employee_manager FOREIGN KEY (manager_id) REFERENCES dtb_manager(id) ON DELETE CASCADE,
    CONSTRAINT UK_EMPLOYEE_MANAGER_ID UNIQUE (id)
);

CREATE INDEX idx_dtb_employee_manager_id ON dtb_employee_manager(id);