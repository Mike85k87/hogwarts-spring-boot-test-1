create table users (
id int primary key,
fisr_name text primary key,
age int,
driving_license boolean,
id_car int REFERENCE car (id)
);

create table cars (
id int primary key,
marka text,
model varchar(128),
prise int
);