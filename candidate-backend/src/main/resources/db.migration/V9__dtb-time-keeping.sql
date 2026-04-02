CREATE TABLE dtb_time_keeping (
    id                  UUID                PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id         UUID                NOT NULL,
    wordDate            DATE                NOT NULL,
    checkIn             TIME                NOT NULL,
    checkOut            TIME                NOT NULL,
    late_minutes        int                 DEFAULT NULL,
    early_leave_minutes int                 DEFAULT NULL,
    status              int                 NOT NULL DEFAULT 0  ,
    version             int                 NOT NULL DEFAULT 0,
    create_at           timestamp(0)        DEFAULT CURRENT_TIMESTAMP,
    update_at           timestamp(0)        DEFAULT CURRENT_TIMESTAMP,
    is_deleted          boolean             DEFAULT false,

    CONSTRAINT UK_TIME_KEEPING_ID UNIQUE (time_keeping_id)
);

CREATE INDEX idx_dtb_time_keeping_id ON dtb_time_keeping(time_keeping_id);
COMMENT ON COLUMN "public"."dtb_time_keeping"."status" IS '0: nghỉ không phép, 1: hợp lệ, 2: đi muộn, 3: về sớm, nghỉ phép';

