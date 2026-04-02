-- Bảng Permission
CREATE TABLE IF NOT EXISTS dtb_permission (
    id              UUID                PRIMARY KEY,
    permission_name VARCHAR(255)        NOT NULL UNIQUE,
    description     TEXT,
    version         int                 NOT NULL DEFAULT 0,
    create_at       timestamp(0)        DEFAULT CURRENT_TIMESTAMP,
    update_at       timestamp(0)        DEFAULT CURRENT_TIMESTAMP,
    is_deleted      boolean             NOT NULL DEFAULT false,
    CONSTRAINT UK_PERMISSION_ID UNIQUE (id)
);
CREATE INDEX idx_dtb_permission_id ON dtb_permission(id);