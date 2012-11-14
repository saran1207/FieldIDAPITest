ALTER TABLE observations ADD COLUMN mobileId VARCHAR(36);

UPDATE observations SET mobileId = uuid();

ALTER TABLE observations MODIFY COLUMN mobileId VARCHAR(36) NOT NULL UNIQUE;