drop table NJANJALICA if exists;
drop table USER if exists;
create table NJANJALICA (ID bigint generated by default as identity unique, NJA_MOZE boolean not null, NJA_NAZIV varchar(255) not null, primary key (ID));
create table USER (ID bigint generated by default as identity unique, password varchar(255) not null unique, username varchar(255) not null unique, primary key (ID));
