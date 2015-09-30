AlTER TABLE saved_items ADD COLUMN procedure_defs_id BIGINT DEFAULT NULL;

ALTER TABLE users ADD COLUMN lastRunProcedureDefsId BIGINT DEFAULT NULL AFTER lastRunProceduresId;

CREATE TABLE saved_procedure_defs (
  id bigINT(21) NOT NULL AUTO_INCREMENT,
  created DATETIME NOT NULL,
  modified DATETIME NOT NULL,
  modifiedby BIGINT DEFAULT NULL,
  createdby BIGINT DEFAULT NULL,
  rfidNumber VARCHAR(255) DEFAULT NULL,
  identifier VARCHAR(255) DEFAULT NULL,
  referenceNumber VARCHAR(255) DEFAULT NULL,
  ownerId BIGINT DEFAULT NULL,
  location VARCHAR(255)  DEFAULT NULL,
  predefinedLocation_id BIGINT DEFAULT NULL,
  assetType BIGINT DEFAULT NULL,
  assetTypeGroup BIGINT DEFAULT NULL,
  procedureType VARCHAR(255) DEFAULT NULL,
  procedureCode VARCHAR(255) DEFAULT NULL,
  publishedState VARCHAR(255) DEFAULT NULL,
  author BIGINT DEFAULT NULL,
  authoredDateRange VARCHAR(255) NULL,
  authoredFromDate DATE DEFAULT NULL,
  authoredToDate DATE DEFAULT NULL,
  modifiedDateRange VARCHAR(255) NULL,
  modifiedFromDate DATE DEFAULT NULL,
  modifiedToDate DATE DEFAULT NULL,
  sortDirection VARCHAR(255) DEFAULT NULL,
  sortColumnId BIGINT DEFAULT NULL,
  query VARCHAR(1024) DEFAULT NULL,
  PRIMARY KEY (id)
);