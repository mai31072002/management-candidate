CREATE TABLE dtb_position (
    id                  UUID                PRIMARY KEY DEFAULT gen_random_uuid(),
    position_name       VARCHAR(100)        NOT NULL,
    description         VARCHAR(254)        DEFAULT NULL,
--    department          VARCHAR(100)        NOT NULL,
    lever_id            UUID                NOT NULL,
    version             int                 NOT NULL DEFAULT 0,
    create_at           timestamp(0)        DEFAULT CURRENT_TIMESTAMP,
    update_at           timestamp(0)        DEFAULT CURRENT_TIMESTAMP,
    is_deleted          boolean             DEFAULT false,

    CONSTRAINT UK_POSITION_ID UNIQUE (position_id)
);

CREATE INDEX idx_dtb_position_id ON dtb_position(position_id);

