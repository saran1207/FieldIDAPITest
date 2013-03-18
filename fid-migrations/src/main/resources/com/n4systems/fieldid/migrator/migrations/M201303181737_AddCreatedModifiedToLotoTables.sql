alter table procedures add column created datetime not null;
alter table procedures add column modified datetime not null;

alter table procedures add column createdby bigint default null;
alter table procedures add column modifiedby bigint default null;

alter table procedure_definitions add column created datetime not null;
alter table procedure_definitions add column modified datetime not null;

alter table procedure_definitions add column createdby bigint default null;
alter table procedure_definitions add column modifiedby bigint default null;

alter table procedures add constraint fk_procedures_created_user foreign key (createdby) references users (id);
alter table procedures add constraint fk_procedures_modified_user foreign key (modifiedby) references users (id);


alter table procedure_definitions add constraint fk_procedure_defs_created_user foreign key (createdby) references users (id);
alter table procedure_definitions add constraint fk_procedure_defs_modified_user foreign key (modifiedby) references users (id);