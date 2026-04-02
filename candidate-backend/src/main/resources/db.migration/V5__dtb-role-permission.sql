-- Bảng trung gian Role-Permission
CREATE TABLE IF NOT EXISTS dtb_role_permission (
    id UUID NOT NULL,
    role_id UUID NOT NULL,
    permission_id UUID NOT NULL,
    version         int                 NOT NULL DEFAULT 0,
    create_at       timestamp(0)        DEFAULT CURRENT_TIMESTAMP,
    update_at       timestamp(0)        DEFAULT CURRENT_TIMESTAMP,
    is_deleted          boolean      NOT NULL DEFAULT false,
    PRIMARY KEY (role_id, permission_id),
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES dtb_role(id) ON DELETE CASCADE,
    CONSTRAINT fk_permission FOREIGN KEY (permission_id) REFERENCES dtb_permission(id) ON DELETE CASCADE,
    CONSTRAINT UK_ROLE_PERMISSION_ID UNIQUE (id)
);
CREATE INDEX idx_dtb_role_permission_id ON dtb_role_permission(id);