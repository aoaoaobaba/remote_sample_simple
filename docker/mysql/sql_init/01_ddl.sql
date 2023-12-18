CREATE DATABASE
 IF NOT EXISTS sample_schema
 CHARACTER SET utf8mb4
 COLLATE utf8mb4_bin;

USE sample_schema;

CREATE TABLE IF NOT EXISTS sample_schema.account(
    account_id    int PRIMARY KEY AUTO_INCREMENT,
    email         varchar(50) NOT NULL,
    password      varchar(30) NOT NULL,
    user_name     varchar(30) NOT NULL,
    INDEX(account_id)
);
