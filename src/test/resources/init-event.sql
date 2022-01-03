--drop table if exists TICKETS;
--drop table if exists ACCOUNTS;
--drop table if exists USERS;
--drop table if exists EVENTS;

--create table USERS (
--	ID			bigint primary key auto_increment,
--	FULL_NAME	varchar (255),
--	EMAIL		varchar (255) unique
--);

create table if not exists EVENTS (
	ID				bigint primary key auto_increment,
	TITLE			varchar (255),
	DATE_HELD		date,
	TICKET_PRICE	numeric (10,2)
);

--create table ACCOUNTS (
--	ID				bigint primary key auto_increment,
--	BALANCE	numeric (10,2) default 0,
--	USER_ID bigint,
--	constraint USERS_FK foreign key (USER_ID) references USERS (ID) on delete cascade
--);
--
--create table TICKETS (
--	ID			bigint primary key auto_increment,
--	USER_ID		bigint,
--	EVENT_ID	bigint,
--	CATEGORY	varchar (32),
--	PLACE 		int,
--	constraint TUSERS_FK foreign key (USER_ID) references USERS (ID) on delete cascade,
--	constraint EVENTS_FK foreign key (EVENT_ID) references EVENTS (ID) on delete cascade
--);
--
--insert into USERS (ID, FULL_NAME, EMAIL)
--		values (1, 'Jules Mcnally', 'Jules_Mcnally8158@extex.org'),
--			   (2, 'Ramon Gray', 'Ramon_Gray3339@gmail.com'),
--			   (3, 'Lucas Bennett', 'Lucas_Bennett6911@eirey.tech'),
--			   (4, 'Madelyn Lucas', 'Madelyn_Lucas7008@bungar.biz'),
--			   (5, 'Tony Reading', 'Tony_Reading4950@supunk.biz'),
--			   (6, 'Carina Edwards', 'Carina_Edwards6585@zorer.org'),
--			   (7, 'Danny Dempsey', 'Danny_Dempsey454@mafthy.com'),
--			   (8, 'Daniel Nelson', 'Daniel_Nelson5389@grannar.com'),
--			   (9, 'Ron Campbell', 'Ron_Campbell6475@atink.com'),
--			   (10, 'Chad Darcy', 'Chad_Darcy7034@yahoo.com');
--
--insert into ACCOUNTS (ID, BALANCE, USER_ID)
--		values 	(1, 100.00, 1),
--				(2, 150.00, 2),
--				(3, 200.00, 3),
--				(4, 60.00, 4),
--				(5, 40.00, 5),
--				(6, 10.00, 6),
--				(7, 0, 7),
--				(8, 100.00, 8),
--				(9, 80.00, 9),
--				(10, 1000.00, 10);

insert into EVENTS (ID, TITLE, DATE_HELD, TICKET_PRICE)
		values	(1, 'Matrix Lucky Hand', '2021-12-15', 15.00),
				(2, 'Glamorous All Poppers', '2022-11-27', 20.00),
				(3, 'Associated Spring', '2022-03-07', 50.00),
				(4, 'Tradecraft Parties', '2023-01-14', 100.00);

--insert into TICKETS (ID, USER_ID, EVENT_ID,	CATEGORY, PLACE)
--		values 	(1, 1, 1, 'STANDARD', 130),
--				(2, 2, 1, 'STANDARD', 139),
--				(3, 3, 3, 'BAR', 98),
--				(4, 4, 4, 'STANDARD', 65),
--				(5, 5, 4, 'PREMIUM', 47),
--				(6, 6, 2, 'BAR', 233),
--				(7, 7, 2, 'STANDARD', 248),
--				(8, 8, 3, 'PREMIUM', 231),
--				(9, 9, 1, 'BAR', 328),
--				(10, 10, 2, 'PREMIUM', 173);