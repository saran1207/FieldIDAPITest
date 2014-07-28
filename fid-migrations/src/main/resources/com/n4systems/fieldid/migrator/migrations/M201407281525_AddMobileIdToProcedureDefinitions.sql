alter table procedure_definitions add column mobileId varchar(36) not null;

alter table procedure_definitions add constraint idx_procedure_definitions_mobileId unique(mobileId);

update procedure_definitions set mobileId = uuid();
