DROP TABLE IF EXISTS park;
create table park (
	park_id		bigint	not null auto_increment,
	park_name	varchar(255),
	longitude	decimal(9,6),
	latitude	decimal(8,6),
	address		varchar(255),
	constraint pk_park primary key (park_id)
);
