CREATE DATABASE IF NOT EXISTS linkshortener;

USE linkshortener;

create table IF NOT EXISTS redirects (
    slug varchar(10) PRIMARY KEY NOT NULL,
    url varchar(1000) NOT NULL
    );