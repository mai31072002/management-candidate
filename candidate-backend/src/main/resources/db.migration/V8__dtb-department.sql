CREATE TABLE dtb_department (
    id                  UUID                PRIMARY KEY DEFAULT gen_random_uuid(),
    department_name     VARCHAR(100)        NOT NULL,
    description         VARCHAR(254)        DEFAULT NULL,
    version             int                 NOT NULL DEFAULT 0,
    create_at           timestamp(0)        DEFAULT CURRENT_TIMESTAMP,
    update_at           timestamp(0)        DEFAULT CURRENT_TIMESTAMP,
    is_deleted          boolean             DEFAULT false,

    CONSTRAINT UK_DEPARTMENT_ID UNIQUE (department_id)
);

CREATE INDEX idx_dtb_department_id ON dtb_department(department_id);

