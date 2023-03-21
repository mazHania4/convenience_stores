DROP DATABASE IF EXISTS convenience_stores;

CREATE DATABASE convenience_stores;
USE convenience_stores;

CREATE TABLE product(
    product_id INTEGER UNSIGNED AUTO_INCREMENT NOT NULL,
    name VARCHAR(70) NOT NULL UNIQUE,
    stock INTEGER NOT NULL,
    cost DECIMAL(6,2) NOT NULL,
    price DECIMAL(6,2) NOT NULL,
    CONSTRAINT pk_product PRIMARY KEY (product_id)
);

CREATE TABLE admin_user(
    user_id INTEGER UNSIGNED AUTO_INCREMENT NOT NULL,
    name VARCHAR(50) NOT NULL,
    username VARCHAR(20)  NOT NULL UNIQUE,
    email VARCHAR(40),
    password VARCHAR(70) NOT NULL,
    CONSTRAINT pk_a_user PRIMARY KEY (user_id)
);

CREATE TABLE supervisor_user(
    user_id INTEGER UNSIGNED AUTO_INCREMENT NOT NULL,
    name VARCHAR(50) NOT NULL,
    username VARCHAR(20)  NOT NULL UNIQUE,
    email VARCHAR(40) NOT NULL,
    password VARCHAR(70) NOT NULL,
    CONSTRAINT pk_sup_user PRIMARY KEY (user_id)
);

CREATE TABLE warehouse_user(
    user_id INTEGER UNSIGNED AUTO_INCREMENT NOT NULL,
    name VARCHAR(50) NOT NULL,
    username VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(40) NOT NULL,
    password VARCHAR(70) NOT NULL,
    CONSTRAINT pk_w_user PRIMARY KEY (user_id)
);

CREATE TABLE store(
    store_id INTEGER UNSIGNED AUTO_INCREMENT NOT NULL,
    w_user_id INTEGER UNSIGNED NOT NULL ,
    address VARCHAR(50) NOT NULL,
    type ENUM('Supervised', 'Not supervised') NOT NULL,
    CONSTRAINT pk_store PRIMARY KEY (store_id),
    CONSTRAINT fk_store_to_w_user
        FOREIGN KEY (w_user_id) REFERENCES warehouse_user(user_id)
);

CREATE TABLE store_user(
    user_id INTEGER UNSIGNED AUTO_INCREMENT NOT NULL,
    store_id INTEGER UNSIGNED NOT NULL,
    name VARCHAR(50) NOT NULL,
    username VARCHAR(20)  NOT NULL UNIQUE,
    email VARCHAR(40) NOT NULL,
    password VARCHAR(70) NOT NULL,
    CONSTRAINT pk_st_user PRIMARY KEY (user_id),
    CONSTRAINT fk_st_user_to_store
        FOREIGN KEY (store_id) REFERENCES store(store_id)
);

CREATE TABLE catalog_item(
    store_id INTEGER UNSIGNED NOT NULL,
    product_id INTEGER UNSIGNED NOT NULL,
    stock INTEGER NOT NULL,
    cost DECIMAL(6,2) NOT NULL,
    price DECIMAL(6,2) NOT NULL,
    CONSTRAINT pk_catalog_item PRIMARY KEY (store_id, product_id),
    CONSTRAINT fk_catalog_to_store
        FOREIGN KEY (store_id) REFERENCES store(store_id),
    CONSTRAINT fk_catalog_to_product
        FOREIGN KEY (product_id) REFERENCES product(product_id)
);

CREATE TABLE damaged_catalog_item(
    store_id INTEGER UNSIGNED NOT NULL,
    product_id INTEGER UNSIGNED NOT NULL,
    stock INTEGER NOT NULL,
    cost DECIMAL(6,2) NOT NULL,
    price DECIMAL(6,2) NOT NULL,
    CONSTRAINT pk_damaged_catalog_item PRIMARY KEY (store_id, product_id),
    CONSTRAINT fk_damaged_catalog_to_store
        FOREIGN KEY (store_id) REFERENCES store(store_id),
    CONSTRAINT fk_damaged_catalog_to_product
        FOREIGN KEY (product_id) REFERENCES product(product_id)
);

CREATE TABLE requisition(
    requisition_id INTEGER UNSIGNED AUTO_INCREMENT NOT NULL,
    store_id INTEGER UNSIGNED NOT NULL,
    st_user_id INTEGER UNSIGNED NOT NULL,
    state ENUM('Requested', 'Pending review', 'Rejected', 'Sent', 'Completed') NOT NULL,
    date_time DATETIME NOT NULL,
    cost DECIMAL(6,2) NOT NULL,
    CONSTRAINT pk_requisition PRIMARY KEY (requisition_id),
    CONSTRAINT fk_requisition_to_store
        FOREIGN KEY (store_id) REFERENCES store(store_id),
    CONSTRAINT fk_requisition_to_st_user
        FOREIGN KEY (st_user_id) REFERENCES store_user(user_id)
);

CREATE TABLE requisition_item(
    requisition_id INTEGER UNSIGNED NOT NULL,
    product_id INTEGER UNSIGNED NOT NULL,
    amount INTEGER NOT NULL,
    unit_cost DECIMAL(6,2) NOT NULL,
    total_cost DECIMAL(6,2) NOT NULL,
    CONSTRAINT pk_requisition_item PRIMARY KEY (requisition_id, product_id),
    CONSTRAINT fk_requisition_item_to_requisition
        FOREIGN KEY (requisition_id) REFERENCES requisition(requisition_id),
    CONSTRAINT fk_requisition_item_to_product
        FOREIGN KEY (product_id) REFERENCES product(product_id)
);

CREATE TABLE shipment(
    shipment_id INTEGER UNSIGNED AUTO_INCREMENT NOT NULL,
    store_id INTEGER UNSIGNED NOT NULL,
    w_user_id INTEGER UNSIGNED NOT NULL,
    requisition_id INTEGER UNSIGNED NOT NULL,
    state ENUM('Dispatched', 'Received') NOT NULL,
    dispatched_date_time DATETIME NOT NULL,
    received_date_time DATETIME,
    cost DECIMAL(6,2) NOT NULL,
    CONSTRAINT pk_shipment PRIMARY KEY (shipment_id),
    CONSTRAINT fk_shipment_to_store
        FOREIGN KEY (store_id) REFERENCES store(store_id),
    CONSTRAINT fk_shipment_to_w_user
        FOREIGN KEY (w_user_id) REFERENCES warehouse_user(user_id),
    CONSTRAINT fk_shipment_to_requisition
        FOREIGN KEY (requisition_id) REFERENCES requisition(requisition_id)
);

CREATE TABLE shipment_item(
    shipment_id INTEGER UNSIGNED NOT NULL,
    product_id INTEGER UNSIGNED NOT NULL,
    amount INTEGER NOT NULL,
    unit_cost DECIMAL(6,2) NOT NULL,
    total_cost DECIMAL(6,2) NOT NULL,
    CONSTRAINT pk_shipment_item PRIMARY KEY (shipment_id, product_id),
    CONSTRAINT fk_shipment_item_to_shipment
        FOREIGN KEY (shipment_id) REFERENCES shipment(shipment_id),
    CONSTRAINT fk_shipment_item_to_product
        FOREIGN KEY (product_id) REFERENCES product(product_id)
);

CREATE TABLE incidence(
    incidence_id INTEGER UNSIGNED AUTO_INCREMENT NOT NULL,
    store_id INTEGER UNSIGNED NOT NULL,
    st_user_id INTEGER UNSIGNED NOT NULL,
    shipment_id INTEGER UNSIGNED NOT NULL,
    state ENUM('Active', 'Solved') NOT NULL,
    date_time DATETIME NOT NULL,
    solution VARCHAR(70),
    CONSTRAINT pk_incidence PRIMARY KEY (incidence_id),
    CONSTRAINT fk_incidence_to_store
        FOREIGN KEY (store_id) REFERENCES store(store_id),
    CONSTRAINT fk_incidence_to_st_user
        FOREIGN KEY (st_user_id) REFERENCES store_user(user_id),
    CONSTRAINT fk_incidence_to_shipment
        FOREIGN KEY (shipment_id) REFERENCES shipment(shipment_id)
);

CREATE TABLE incidence_item(
    incidence_id INTEGER UNSIGNED NOT NULL,
    product_id INTEGER UNSIGNED NOT NULL,
    amount INTEGER NOT NULL,
    reason ENUM('Wrong product', 'Damaged product', 'Unsolicited product', 'Missing product', 'Excess product')  NOT NULL,
    CONSTRAINT pk_shipment_item PRIMARY KEY (incidence_id, product_id),
    CONSTRAINT fk_incidence_item_to_incidence
        FOREIGN KEY (incidence_id) REFERENCES incidence(incidence_id),
    CONSTRAINT fk_incidence_item_to_product
        FOREIGN KEY (product_id) REFERENCES product(product_id)
);

CREATE TABLE devolution(
    devolution_id INTEGER UNSIGNED AUTO_INCREMENT NOT NULL,
    store_id INTEGER UNSIGNED NOT NULL,
    st_user_id INTEGER UNSIGNED NOT NULL,
    shipment_id INTEGER UNSIGNED NOT NULL,
    state ENUM('Active', 'Rejected', 'Accepted') NOT NULL,
    date_time DATETIME NOT NULL,
    cost DECIMAL(6,2) NOT NULL,
    CONSTRAINT pk_devolution PRIMARY KEY (devolution_id),
    CONSTRAINT fk_devolution_to_store
        FOREIGN KEY (store_id) REFERENCES store(store_id),
    CONSTRAINT fk_devolution_to_st_user
        FOREIGN KEY (st_user_id) REFERENCES store_user(user_id),
    CONSTRAINT fk_devolution_to_shipment
        FOREIGN KEY (shipment_id) REFERENCES shipment(shipment_id)
);

CREATE TABLE devolution_item(
    devolution_id INTEGER UNSIGNED NOT NULL,
    product_id INTEGER UNSIGNED NOT NULL,
    amount INTEGER NOT NULL,
    unit_cost DECIMAL(6,2) NOT NULL,
    total_cost DECIMAL(6,2) NOT NULL,
    reason ENUM('Wrong product', 'Damaged product', 'Unsolicited product', 'Excess product')  NOT NULL,
    CONSTRAINT pk_devolution_item PRIMARY KEY (devolution_id, product_id),
    CONSTRAINT fk_devolution_item_to_devolution
        FOREIGN KEY (devolution_id) REFERENCES devolution(devolution_id),
    CONSTRAINT fk_devolution_item_to_product
        FOREIGN KEY (product_id) REFERENCES product(product_id)
);


