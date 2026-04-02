-- Bảng trung gian User-Role
CREATE TABLE IF NOT EXISTS dtb_user_role (
    id UUID NOT NULL,
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    version         int                 NOT NULL DEFAULT 0,
    create_at       timestamp(0)        DEFAULT CURRENT_TIMESTAMP,
    update_at       timestamp(0)        DEFAULT CURRENT_TIMESTAMP,
    is_deleted          boolean      NOT NULL DEFAULT false,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES dtb_user(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_role_user FOREIGN KEY (role_id) REFERENCES dtb_role(id) ON DELETE CASCADE,
    CONSTRAINT UK_USER_ROLE_ID UNIQUE (id)
);

CREATE INDEX idx_dtb_role_user_id ON dtb_user_role(id);