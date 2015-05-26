ALTER TABLE ASSETS
ADD FULLTEXT INDEX 'fulltext' ('identifier' ASC, 'customerRefNumber' ASC, 'rfidNumber' ASC);