alter table procedures add column state varchar(255) not null;
update procedures set state='ACTIVE';

alter table procedure_definitions add column state varchar(255) not null;
update procedure_definitions set state='ACTIVE';