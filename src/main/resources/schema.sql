CREATE TABLE IF NOT EXISTS region
(
    id
    SERIAL
    CONSTRAINT
    region_pkey
    PRIMARY
    KEY,
    name
    TEXT
    NOT
    NULL
);
CREATE INDEX IF NOT EXISTS region_name_idx ON region(name);

CREATE TABLE IF NOT EXISTS country
(
    id
    SERIAL
    CONSTRAINT
    country_pkey
    PRIMARY
    KEY,
    name
    TEXT
    NOT
    NULL
);
CREATE INDEX IF NOT EXISTS country_name_idx ON region(name);

CREATE TABLE IF NOT EXISTS item_type
(
    id
    SERIAL
    CONSTRAINT
    item_type_pkey
    PRIMARY
    KEY,
    name
    TEXT
    NOT
    NULL
);
CREATE INDEX IF NOT EXISTS item_type_name_idx ON region(name);

CREATE TABLE IF NOT EXISTS sales_order
(
    id
    BIGSERIAL
    PRIMARY
    KEY,
    uuid
    UUID
    NOT
    NULL
    UNIQUE,
    region_id
    INTEGER
    NOT
    NULL
    CONSTRAINT
    region_fk
    references
    region
(
    id
),
    country_id INTEGER NOT NULL CONSTRAINT country_fk references country
(
    id
),
    item_type_id INTEGER NOT NULL CONSTRAINT item_type_fk references item_type
(
    id
),
    sales_channel VARCHAR
(
    1
) NOT NULL,
    priority VARCHAR
(
    1
) NOT NULL,
    date DATE NOT NULL,
    ship_date DATE NOT NULL,
    units_sold INTEGER NOT NULL,
    unit_price DECIMAL
(
    5,
    2
) NOT NULL,
    unit_cost DECIMAL
(
    5,
    2
) NOT NULL,
    total_revenue DECIMAL
(
    5,
    2
) NOT NULL,
    total_cost DECIMAL
(
    5,
    2
) NOT NULL,
    total_profit DECIMAL
(
    5,
    2
) NOT NULL
    );
