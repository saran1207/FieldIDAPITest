ALTER TABLE procedure_definitions ADD rejected_date datetime;
ALTER TABLE procedure_definitions ADD rejected_by_id bigint(20);
ALTER TABLE procedure_definitions ADD rejected_reason varchar(1024);