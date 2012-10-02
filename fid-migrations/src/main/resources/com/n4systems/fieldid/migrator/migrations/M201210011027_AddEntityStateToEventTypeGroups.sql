ALTER TABLE eventtypegroups ADD state VARCHAR(255);

UPDATE eventtypegroups SET state='ACTIVE';