CREATE TABLE dtb_employee (
    employee_id        UUID                PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name          VARCHAR(100)        DEFAULT NULL,
    last_name           VARCHAR(100)        DEFAULT NULL,
    full_name           VARCHAR(100)        DEFAULT NULL,
    province            VARCHAR(100)        NOT NULL,
    district            VARCHAR(100)        NOT NULL,
    address             VARCHAR(255)        NOT NULL,
    gender              int                 NOT NULL,
    birthday            DATE                NOT NULL,
    phone               VARCHAR(15)         DEFAULT NULL,
    cccd                VARCHAR(20)         DEFAULT NULL,
    description         TEXT                DEFAULT NULL,
    start_date          DATE                NOT NULL,
    end_date            DATE                DEFAULT NULL,
    status              int                 NOT NULL DEFAULT 1,
    position_id         UUID                NOT NULL,
    department_id          VARCHAR(100)        NOT NULL,
    employees_code      VARCHAR(50)         NOT NULL,
    baseSalary          NUMERIC(15, 2)      NOT NULL,
    allowance           NUMERIC(15, 2)      NOT NULL,
    version             int                 NOT NULL DEFAULT 0,
    create_at           timestamp(0)        DEFAULT CURRENT_TIMESTAMP,
    update_at           timestamp(0)        DEFAULT CURRENT_TIMESTAMP,
    is_deleted          boolean             DEFAULT false,

    CONSTRAINT UK_EMPLOYEE_ID UNIQUE (employee_id)
);

CREATE INDEX idx_dtb_employee_id ON employees(employee_id);
CREATE INDEX idx_employees_fullname_unaccent_lower ON dtb_employees (public.unaccent(lower(full_name::text)))
WHERE is_deleted = false;
COMMENT ON COLUMN "public"."dtb_employees"."gender" IS '0: nữ, 1: nam';
COMMENT ON COLUMN "public"."dtb_employees"."status" IS '1: đang làm việc, 2: đã nghỉ việc, 3: thử việc';

