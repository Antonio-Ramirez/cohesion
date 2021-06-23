CREATE SCHEMA devices;

CREATE TABLE IF NOT EXISTS devices.device
(
    device_id uuid NOT NULL,
    "name" character varying(100) NOT NULL,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_device PRIMARY KEY (device_id)
);

CREATE TABLE IF NOT EXISTS devices.raw_data
(
    id serial NOT NULL,
    device_id uuid NOT NULL,
    current_value numeric(6,4) NOT NULL,
    unit character varying(50) NOT NULL,
    "timestamp" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version numeric(3,2) NOT NULL
);