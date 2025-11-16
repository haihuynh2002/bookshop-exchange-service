CREATE TABLE exchange (
      id BIGSERIAL PRIMARY KEY NOT NULL,
      order_id BIGINT NOT NULL,
      condition varchar(255) NOT NULL,
      reason varchar(255) NOT NULL,
      status varchar(255) NOT NULL,

      email VARCHAR(255) NOT NULL,
      phone VARCHAR(255),
      first_name VARCHAR(255) NOT NULL,
      last_name VARCHAR(255) NOT NULL,

      created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      last_modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      created_by VARCHAR(255) NOT NULL,
      last_modified_by VARCHAR(255),
      version INTEGER NOT NULL DEFAULT 0
);