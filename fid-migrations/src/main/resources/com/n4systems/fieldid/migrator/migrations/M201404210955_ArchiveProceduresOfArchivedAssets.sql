UPDATE procedure_definitions
SET state = 'ARCHIVED'
WHERE id in (SELECT * from (SELECT pd.id
                             FROM procedure_definitions pd, assets a
                             WHERE pd.asset_id = a.id and a.state = 'ARCHIVED') as T);

UPDATE procedures
SET state = 'ARCHIVED'
WHERE id in (SELECT * from (SELECT p.id
                            FROM procedures p, assets a
                            WHERE p.asset_id = a.id and a.state = 'ARCHIVED') as T);

UPDATE procedure_definitions
SET state = 'ARCHIVED'
WHERE id in (SELECT * from (SELECT pd.id
                            FROM procedure_definitions pd, assets a, assettypes at
                            WHERE pd.asset_id = a.id and a.type_id = at.id and at.state = 'ARCHIVED') as T);

UPDATE procedures
SET state = 'ARCHIVED'
WHERE id in (SELECT * from (SELECT p.id
                            FROM procedures p, assets a, assettypes at
                            WHERE p.asset_id = a.id and a.type_id = at.id  and at.state = 'ARCHIVED') as T);