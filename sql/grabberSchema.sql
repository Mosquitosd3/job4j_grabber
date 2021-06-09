create schema if not exists grabber;
create table grabber.post(
	id serial primary key,
	name varchar(255),
	text text,
	link varchar(255) not null unique,
	created timestamp
);