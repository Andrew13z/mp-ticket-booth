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
		values (1, 'Jules Mcnally', 'Jules_Mcnally8158@extex.org'),
			   (2, 'Ramon Gray', 'Ramon_Gray3339@gmail.com'),
			   (3, 'Lucas Bennett', 'Lucas_Bennett6911@eirey.tech'),
			   (4, 'Madelyn Lucas', 'Madelyn_Lucas7008@bungar.biz'),
			   (5, 'Tony Reading', 'Tony_Reading4950@supunk.biz'),
			   (6, 'Carina Edwards', 'Carina_Edwards6585@zorer.org'),
			   (7, 'Danny Dempsey', 'Danny_Dempsey454@mafthy.com'),
			   (8, 'Daniel Nelson', 'Daniel_Nelson5389@grannar.com'),
			   (9, 'Ron Campbell', 'Ron_Campbell6475@atink.com'),
			   (10, 'Chad Darcy', 'Chad_Darcy7034@yahoo.com');

insert into ACCOUNTS (ID, BALANCE, USER_ID)
		values 	(1, 100.00, 1),
				(2, 150.00, 2),
				(3, 200.00, 3),
				(4, 60.00, 4),
				(5, 40.00, 5),
				(6, 10.00, 6),
				(7, 0, 7),
				(8, 100.00, 8),
				(9, 80.00, 9),
				(10, 1000.00, 10);

insert into EVENTS (ID, TITLE, DATE_HELD, TICKET_PRICE)
		values	(1, 'Matrix Lucky Hand', '2021-12-15', 15.00),
				(2, 'Glamorous All Poppers', '2022-11-27', 20.00),
				(3, 'Associated Spring', '2022-03-07', 50.00),
				(4, 'Tradecraft Parties', '2023-01-14', 100.00);