CREATE TABLE dtb_ot_date (
    id                  UUID                PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id          UUID                NOT NULL,
    workDate            DATE                NOT NULL,
    ot_type             int                 NOT NULL,
    ot_minutes          int                NOT NULL,
    start_time          TIME                NOT NULL,
    endTime             TIME                NOT NULL,
    jobTitle            VARCHAR(100)        NOT NULL,
    approved_by         VARCHAR(100)        NOT NULL,
    approved_at         timestamp(0)        NOT NULL,
    ot_rate             NUMERIC(10,2)       NOT NULL,
    status              int                 NOT NULL DEFAULT 0,
    version             int                 NOT NULL DEFAULT 0,
    create_at           timestamp(0)        DEFAULT CURRENT_TIMESTAMP,
    update_at           timestamp(0)        DEFAULT CURRENT_TIMESTAMP,
    is_deleted          boolean             DEFAULT false,

    CONSTRAINT UK_OT_DATE_ID UNIQUE (ot_date_id)
);

CREATE INDEX idx_dtb_ot_date_id ON dtb_ot_date(ot_date_id);
COMMENT ON COLUMN "public"."dtb_ot_date"."status" IS '0: chờ duyệt, 1: đã duyệt, 2: không được duyêt';