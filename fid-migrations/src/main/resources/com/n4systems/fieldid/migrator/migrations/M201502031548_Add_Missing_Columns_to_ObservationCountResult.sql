alter TABLE observationcount_result
add column created datetime NOT NULL,
add column modified datetime NOT NULL,
add column modifiedby bigint(20) DEFAULT NULL,
add column createdby bigint(20) DEFAULT NULL,
add column tenant_id bigint(20) NOT NULL,
add column state varchar(255) NOT NULL;