
-- Bảng Role
CREATE TABLE IF NOT EXISTS dtb_role (
    id              UUID                PRIMARY KEY,
    role_name       VARCHAR(255)        NOT NULL UNIQUE,
    description     TEXT,
    version         int                 NOT NULL DEFAULT 0,
    create_at       timestamp(0)        DEFAULT CURRENT_TIMESTAMP,
    update_at       timestamp(0)        DEFAULT CURRENT_TIMESTAMP,
    is_deleted      boolean             NOT NULL DEFAULT false,
    CONSTRAINT UK_RODE_ID UNIQUE (id)
);

CREATE INDEX idx_dtb_role_id ON dtb_role(id);