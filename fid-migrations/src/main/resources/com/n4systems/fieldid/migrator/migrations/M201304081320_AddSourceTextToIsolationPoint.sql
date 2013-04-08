alter table isolation_points add column source_text varchar(250);

#fix broken column defs & constraints while i'm at it.
alter table isolation_points modify column createdby bigint;
alter table isolation_points modify column modifiedby bigint;

alter table procedure_definitions_isolation_points drop foreign key fk_isolation_points;
alter table procedure_definitions_isolation_points add constraint fk_isolation_points foreign key (isolation_point_id) references isolation_points(id);
