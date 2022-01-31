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

insert into ACCOUNTS (ID, BALANCE)
    values 	(1, 100.00),
            (2, 150.00),
            (3, 200.00),
            (4, 60.00),
            (5, 40.00),
            (6, 10.00),
            (7, 190.00),
            (8, 100.00),
            (9, 80.00),
            (10, 1000.00);

insert into EVENTS (TITLE, DATE_HELD, TICKET_PRICE)
		values	('Matrix Lucky Hand', '2021-12-15', 15.00),
				('Glamorous All Poppers', '2022-11-27', 20.00),
				('Associated Spring', '2022-03-07', 50.00),
				('Tradecraft Parties', '2023-01-14', 100.00);

insert into TICKETS (USER_ID, EVENT_ID,	CATEGORY, PLACE)
		values 	(1, 1, 'STANDARD', 130),
				(2, 1, 'STANDARD', 139),
				(3, 3, 'BAR', 98),
				(4, 4, 'STANDARD', 65),
				(5, 4, 'PREMIUM', 47),
				(6, 2, 'BAR', 233),
				(7, 2, 'STANDARD', 248),
				(8, 3, 'PREMIUM', 231),
				(9, 1, 'BAR', 328),
				(10, 2, 'PREMIUM', 173);
