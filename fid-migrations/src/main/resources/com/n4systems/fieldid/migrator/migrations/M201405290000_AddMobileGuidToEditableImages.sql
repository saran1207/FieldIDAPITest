alter table fileattachments add column mobileid char(36) not null;
update fileattachments set mobileid = uuid();
