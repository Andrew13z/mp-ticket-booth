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