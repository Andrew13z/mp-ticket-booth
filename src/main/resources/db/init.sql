create schema TICKET_BOOTH;
set search_path to TICKET_BOOTH;

drop sequence if exists USERS_ID_SEQ;
create sequence USERS_ID_SEQ;

drop table if exists USERS;
create table USERS (
	ID			bigint primary key default nextval('USERS_ID_SEQ'),
	FULL_NAME	varchar (255),
	EMAIL		varchar (255) unique
);

drop sequence if exists EVENTS_ID_SEQ;
create sequence EVENTS_ID_SEQ;

create table if exists EVENTS (
	ID				bigint primary key default nextval('EVENTS_ID_SEQ'),
	TITLE			varchar (255),
	DATE_HELD		date,
	TICKET_PRICE	numeric (10,2)
);

drop sequence if exists ACCOUNTS_ID_SEQ;
create sequence ACCOUNTS_ID_SEQ;

drop table if exists ACCOUNTS;
create table ACCOUNTS (
	ID				bigint primary key default nextval('ACCOUNTS_ID_SEQ'),
	BALANCE	numeric (10,2),
	USER_ID bigint,
	constraint USERS_FK foreign key (USER_ID) references USERS (ID)
);

drop sequence if exists TICKETS_ID_SEQ;
create sequence TICKETS_ID_SEQ;

drop table if exists TICKETS;
create table TICKETS (
	ID			bigint primary key default nextval('TICKETS_ID_SEQ'),
	USER_ID		bigint,
	EVENT_ID	bigint,
	CATEGORY	varchar (32),
	PLACE 		int,
	constraint USERS_FK foreign key (USER_ID) references USERS (ID),
	constraint EVENTS_FK foreign key (EVENT_ID) references EVENTS (ID)
);
	