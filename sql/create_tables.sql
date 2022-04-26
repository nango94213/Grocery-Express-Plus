-- Add real tables in here
DROP TABLE IF EXISTS public.log_events;

CREATE TABLE public.log_events (
    log_id VARCHAR(50) NOT NULL,
    command_type VARCHAR(50) NOT NULL,
    level VARCHAR(10) NOT NULL,
    message varchar(1000) NOT NULL,
    creation_date DATE NOT NULL,

    PRIMARY KEY(log_id)
);

DROP TABLE IF EXISTS public.log_transactions;

CREATE TABLE public.log_transactions (
    log_id VARCHAR(50) NOT NULL,
    storename VARCHAR(50) NOT NULL,
    itemname VARCHAR(50) NOT NULL,
    unitprice INT NOT NULL,
    quantity INT NOT NULL,
    customerusername varchar(50) NOT NULL,
    creation_date DATE NOT NULL,

    PRIMARY KEY(log_id)
);

-- START AUTHORIZATION
DROP TABLE IF EXISTS role;

\echo CREATE TABLE role
CREATE TABLE role (
    role_name VARCHAR(50) NOT NULL UNIQUE,

    PRIMARY KEY(role_name)
);

DROP TABLE IF EXISTS instruction;

\echo CREATE TABLE instruction
CREATE TABLE instruction (
    inst_name VARCHAR(50) NOT NULL UNIQUE,

    PRIMARY KEY(inst_name)
);

DROP TABLE IF EXISTS rolepermission;

\echo CREATE TABLE rolepermission
CREATE TABLE rolepermission (
    rp_id serial,
    role_name VARCHAR(50) NOT NULL,
    inst_name VARCHAR(50) NOT NULL,

    PRIMARY KEY(rp_id),
    FOREIGN KEY(role_name) REFERENCES role(role_name),
    FOREIGN KEY(inst_name) REFERENCES instruction(inst_name)
);
-- END AUTHORIZATION


-- START AUTHENTICATION
DROP TABLE IF EXISTS appuser;

\echo CREATE TABLE appuser
CREATE TABLE appuser (
    user_id VARCHAR(50) NOT NULL UNIQUE,
    role_name VARCHAR(50) NOT NULL,
    password VARCHAR(1000) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(50) NOT NULL,

    PRIMARY KEY(user_id),
    FOREIGN KEY(role_name) REFERENCES role(role_name)
);

DROP TABLE IF EXISTS customer;

\echo CREATE TABLE customer
CREATE TABLE customer (

    customer_id serial,
    account_id VARCHAR(50) NOT NULL UNIQUE,
    appuser_id VARCHAR(50) NOT NULL UNIQUE,
    rating int NOT NULL,
    credit int NOT NULL,

    PRIMARY KEY(account_id),
    FOREIGN KEY(appuser_id) REFERENCES appuser(user_id)
);

DROP TABLE IF EXISTS pilot;

\echo CREATE TABLE pilot
CREATE TABLE pilot (

  pilot_id serial,
  account_id VARCHAR(50) NOT NULL UNIQUE,
  appuser_id VARCHAR(50) NOT NULL UNIQUE,
  license_id VARCHAR(50) NOT NULL UNIQUE,
  tax_id VARCHAR(50) NOT NULL,
  months_experience int NOT NULL,
  salary int NOT NULL,
  experience int NOT NULL,

  PRIMARY KEY(account_id),
  FOREIGN KEY(appuser_id) REFERENCES appuser(user_id)
);
-- END AUTHENTICATION

DROP TABLE IF EXISTS expressstore;

-- START STORE
\echo CREATE TABLE expressstore
CREATE TABLE expressstore (
    name VARCHAR(50) NOT NULL UNIQUE,
    storeownerusername VARCHAR(50) NOT NULL,
    revenue INT NOT NULL,

    PRIMARY KEY (name),
    FOREIGN KEY(storeownerusername) REFERENCES appuser(user_id)
);
-- END STORE

DROP TABLE IF EXISTS expressstoreitem;

-- START ITEM
\echo CREATE TABLE expressstoreitem
CREATE TABLE expressstoreitem (
    name VARCHAR(50) NOT NULL,
    storename VARCHAR(50) NOT NULL,
    category VARCHAR(50) NOT NULL,
    weight INT NOT NULL,

    PRIMARY KEY (name, storename),
    FOREIGN KEY(storename) REFERENCES expressstore(name)
);
-- END ITEM

-- START configuration
DROP TABLE IF EXISTS expressconfiguration;

\echo CREATE TABLE expressconfiguration
CREATE TABLE expressconfiguration (
    name VARCHAR(50) NOT NULL UNIQUE,
    storename VARCHAR(50),
    type VARCHAR(50) NOT NULL,
    category VARCHAR(50),
    quota INT,
    active BOOLEAN,

    PRIMARY KEY (name),
    FOREIGN KEY(storename) REFERENCES expressstore(name)
);
-- END configuration

DROP TABLE IF EXISTS storeconfiguration;

-- START storeconfiguration
\echo CREATE TABLE storeconfiguration
CREATE TABLE storeconfiguration (
    storename VARCHAR(50) NOT NULL,
    configurationname VARCHAR(50) NOT NULL,

    PRIMARY KEY(storename, configurationname),
    FOREIGN KEY(storename) REFERENCES expressstore(name),
    FOREIGN KEY(configurationname) REFERENCES expressconfiguration(name)
);
-- END storeconfiguration

DROP TABLE IF EXISTS itemconfiguration;

-- START itemconfiguration
\echo CREATE TABLE itemconfiguration
CREATE TABLE itemconfiguration (
    storename VARCHAR(50) NOT NULL,
    itemname VARCHAR(50) NOT NULL,
    configurationname VARCHAR(50) NOT NULL,

    PRIMARY KEY(storename, itemname, configurationname),
    FOREIGN KEY(storename) REFERENCES expressstore(name),
    FOREIGN KEY(configurationname) REFERENCES expressconfiguration(name)
);
-- END itemconfiguration