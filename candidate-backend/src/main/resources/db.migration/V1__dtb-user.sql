CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS dtb_user (
    user_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username        VARCHAR(254)        NOT NULL UNIQUE,
    password        VARCHAR(254)        NOT NULL,
    email           VARCHAR(255)        DEFAULT NULL UNIQUE,
    employeeId      UUID                NOT NULL UNIQUE,

    version         int                 NOT NULL DEFAULT 0,
    create_at       timestamp(0)        DEFAULT CURRENT_TIMESTAMP,
    update_at       timestamp(0)        DEFAULT CURRENT_TIMESTAMP,
    is_deleted          boolean      NOT NULL DEFAULT false,

--    Thêm constraint để đảm bảo giá trị key là duy nhât
--    Có thể thừa vì primary key đã có yêu cầu unique rồi nhưng tôi vẵn tạo với mục đích đặt tên rõ ràng
        CONSTRAINT UK_USER_ID UNIQUE (user_id)
);

--  Tạo index trên cột user_id để tăng tốc các truy vấn lọc hoặc join theo user_id.
CREATE INDEX idx_dtb_user_id ON dtb_user(user_id);


-- hỗ trợ search không phân biệt chữ hoa chữ thường
CREATE EXTENSION IF NOT EXISTS unaccent;