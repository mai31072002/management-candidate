CREATE TABLE dtb_salaries (
    id                  UUID                PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id        UUID                NOT NULL,
    month               VARCHAR(7)          NOT NULL, -- YYYY-MM
    base_salary         NUMERIC(15,2)       NOT NULL,
    total_work_days     int                 DEFAULT NULL,
    ot_amount           NUMERIC(15,2)       DEFAULT NULL,
    allowance           NUMERIC(15,2)       DEFAULT NULL,
    deductions          NUMERIC(15,2)       DEFAULT NULL,
    net_salary          NUMERIC(15,2)       DEFAULT NULL,

    version             int                 NOT NULL DEFAULT 0,
    create_at           timestamp(0)        DEFAULT CURRENT_TIMESTAMP,
    update_at           timestamp(0)        DEFAULT CURRENT_TIMESTAMP,
    is_deleted          boolean             DEFAULT false,

    CONSTRAINT UK_SALARIES_ID UNIQUE (salaries_id)
);

CREATE INDEX idx_dtb_salaries_id ON dtb_salaries(salaries_id);
