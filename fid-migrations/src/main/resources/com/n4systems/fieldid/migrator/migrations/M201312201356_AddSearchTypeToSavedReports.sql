alter table saved_reports add column search_type varchar(20) not null;

update saved_reports set search_type="EVENTS";