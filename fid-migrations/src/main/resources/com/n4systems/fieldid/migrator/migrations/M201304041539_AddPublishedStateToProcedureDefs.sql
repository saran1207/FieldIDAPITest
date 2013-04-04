alter table procedure_definitions add column published_state varchar(255) not null;
update procedure_definitions set published_state = "PUBLISHED";