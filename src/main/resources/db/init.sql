create schema TICKET_BOOTH;
set search_path to TICKET_BOOTH;

drop table if exists TICKETS;
drop sequence if exists TICKETS_ID_SEQ;

drop table if exists ACCOUNTS;
drop sequence if exists ACCOUNTS_ID_SEQ;

drop table if exists EVENTS;
drop sequence if exists EVENTS_ID_SEQ;

drop table if exists USERS;
drop sequence if exists USERS_ID_SEQ;

create sequence USERS_ID_SEQ no maxvalue start with 1 increment by 1;

create table USERS (
	ID			bigint primary key default nextval('USERS_ID_SEQ'),
	FULL_NAME	varchar (255),
	EMAIL		varchar (255) unique
);

create sequence EVENTS_ID_SEQ no maxvalue start with 1 increment by 1;

create table EVENTS (
	ID				bigint primary key default nextval('EVENTS_ID_SEQ'),
	TITLE			varchar (255),
	DATE_HELD		date,
	TICKET_PRICE	numeric (10,2)
);

create sequence ACCOUNTS_ID_SEQ no maxvalue start with 1 increment by 1;

create table ACCOUNTS (
	ID				bigint primary key default nextval('ACCOUNTS_ID_SEQ'),
	BALANCE	numeric (10,2) default 0,
	USER_ID bigint,
	constraint USERS_FK foreign key (USER_ID) references USERS (ID)
);

create sequence TICKETS_ID_SEQ no maxvalue start with 1 increment by 1;

create table TICKETS (
	ID			bigint primary key default nextval('TICKETS_ID_SEQ'),
	USER_ID		bigint,
	EVENT_ID	bigint,
	CATEGORY	varchar (32),
	PLACE 		int,
	constraint USERS_FK foreign key (USER_ID) references USERS (ID),
	constraint EVENTS_FK foreign key (EVENT_ID) references EVENTS (ID)
);

insert into USERS (FULL_NAME, EMAIL)
		values ('Jules Mcnally', 'Jules_Mcnally8158@extex.org'),
			   ('Ramon Gray', 'Ramon_Gray3339@gmail.com'),
			   ('Lucas Bennett', 'Lucas_Bennett6911@eirey.tech'),
			   ('Madelyn Lucas', 'Madelyn_Lucas7008@bungar.biz'),
			   ('Tony Reading', 'Tony_Reading4950@supunk.biz'),
			   ('Carina Edwards', 'Carina_Edwards6585@zorer.org'),
			   ('Danny Dempsey', 'Danny_Dempsey454@mafthy.com'),
			   ('Daniel Nelson', 'Daniel_Nelson5389@grannar.com'),
			   ('Ron Campbell', 'Ron_Campbell6475@atink.com'),
			   ('Chad Darcy', 'Chad_Darcy7034@yahoo.com');
			  
insert into ACCOUNTS (BALANCE)
		values 	(100.00),
				(150.00),
				(200.00),
				(60.00),
				(40.00),
				(10.00),
				(0),
				(100.00),
				(80.00),
				(1000.0);
			
insert into EVENTS (TITLE, DATE_HELD, TICKET_PRICE)
		values	('Matrix Lucky Hand', '2021-12-15', 15.00),
				('Glamorous All Poppers', '2022-11-27', 20.00),
				('Associated Spring', '2022-03-07', 50.00),
				('Tradecraft Parties', '2023-01-14', 100.00);
			
insert into TICKETS (USER_ID, EVENT_ID,	CATEGORY, PLACE);



