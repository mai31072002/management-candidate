
-- Bảng Role
CREATE TABLE IF NOT EXISTS dtb_lever (
    id              UUID                PRIMARY KEY,
    lever_number    int                 NOT NULL UNIQUE,
    description     VARCHAR(254)        DEFAULT NULL,
    version         int                 NOT NULL DEFAULT 0,
    create_at       timestamp(0)        DEFAULT CURRENT_TIMESTAMP,
    update_at       timestamp(0)        DEFAULT CURRENT_TIMESTAMP,
    is_deleted      boolean             NOT NULL DEFAULT false,
    CONSTRAINT UK_LEVER_ID UNIQUE (id)
);

CREATE INDEX idx_dtb_lever_id ON dtb_lever(id);