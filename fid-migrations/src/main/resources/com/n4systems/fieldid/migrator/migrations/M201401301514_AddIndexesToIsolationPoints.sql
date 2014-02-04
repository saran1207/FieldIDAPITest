ALTER TABLE isolation_points ADD COLUMN fwd_idx BIGINT;
ALTER TABLE isolation_points ADD COLUMN rev_idx BIGINT;

UPDATE isolation_points I SET fwd_idx = (SELECT orderIdx FROM procedure_definitions_isolation_points P WHERE P.isolation_point_id = I.id);