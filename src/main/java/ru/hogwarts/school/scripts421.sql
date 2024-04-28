alter TABLE student ADD CONSTRAINT age_check CHECK (age>= 16);

alter table student add constraint name_unique unique (name);

alter table student alter column name set not null;

alter table faculty add constraint name_color_unique unique (name, color);

alter table student alter column age set default 20;