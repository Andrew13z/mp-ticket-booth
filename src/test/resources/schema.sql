drop table if exists ACCOUNTS;
drop table if exists USERS;
drop sequence if exists USERS_ID_SEQ;
drop table if exists EVENTS;
drop sequence if exists EVENTS_ID_SEQ;
drop table if exists TICKETS;
drop sequence if exists TICKETS_ID_SEQ;

create sequence USERS_ID_SEQ;

create table USERS (
	ID			bigint default USERS_ID_SEQ.nextval primary key,
	FULL_NAME	varchar (255),
	EMAIL		varchar (255) unique
);

create table ACCOUNTS (
	ID				bigint primary key,
	BALANCE	numeric (10,2) default 0,
	USER_ID bigint,
	constraint USERS_FK foreign key (USER_ID) references USERS (ID) on delete cascade
);

create sequence EVENTS_ID_SEQ;

create table EVENTS (
	ID			bigint default EVENTS_ID_SEQ.nextval primary key,
	TITLE			varchar (255),
	DATE_HELD		date,
	TICKET_PRICE	numeric (10,2)
);

create sequence TICKETS_ID_SEQ increment by 10;

create table TICKETS (
	ID			bigint default TICKETS_ID_SEQ.nextval primary key,
	USER_ID		bigint,
	EVENT_ID	bigint,
	CATEGORY	varchar (32),
	PLACE 		int,
	constraint TUSERS_FK foreign key (USER_ID) references USERS (ID) on delete cascade,
	constraint EVENTS_FK foreign key (EVENT_ID) references EVENTS (ID) on delete cascade
);

