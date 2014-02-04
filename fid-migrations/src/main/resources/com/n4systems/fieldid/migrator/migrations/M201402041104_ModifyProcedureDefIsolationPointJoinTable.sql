ALTER TABLE isolation_points MODIFY COLUMN fwd_idx BIGINT(20) NOT NULL;
ALTER TABLE isolation_points MODIFY COLUMN rev_idx BIGINT(20) NOT NULL;

ALTER TABLE procedure_definitions_isolation_points DROP PRIMARY KEY, ADD PRIMARY KEY (procedure_definition_id, isolation_point_id);

ALTER TABLE procedure_definitions_isolation_points MODIFY COLUMN orderIdx BIGINT(20) NULL;