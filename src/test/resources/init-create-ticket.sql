create table USERS (
	ID			bigint primary key auto_increment,
	FULL_NAME	varchar (255),
	EMAIL		varchar (255) unique
);

create table ACCOUNTS (
	ID				bigint primary key auto_increment,
	BALANCE	numeric (10,2) default 0,
	USER_ID bigint,
	constraint USERS_FK foreign key (USER_ID) references USERS (ID) on delete cascade
);

create table if not exists EVENTS (
	ID				bigint primary key auto_increment,
	TITLE			varchar (255),
	DATE_HELD		date,
	TICKET_PRICE	numeric (10,2)
);

create table TICKETS (
	ID			bigint primary key auto_increment,
	USER_ID		bigint,
	EVENT_ID	bigint,
	CATEGORY	varchar (32),
	PLACE 		int,
	constraint TUSERS_FK foreign key (USER_ID) references USERS (ID) on delete cascade,
	constraint EVENTS_FK foreign key (EVENT_ID) references EVENTS (ID) on delete cascade
);

insert into USERS (ID, FULL_NAME, EMAIL)
		values (1, 'Jules Mcnally', 'Jules_Mcnally8158@extex.org');

insert into ACCOUNTS (ID, BALANCE)
		values 	(1, 100.00);

insert into EVENTS (ID, TITLE, DATE_HELD, TICKET_PRICE)
		values	(1, 'Matrix Lucky Hand', '2021-12-15', 15.00);