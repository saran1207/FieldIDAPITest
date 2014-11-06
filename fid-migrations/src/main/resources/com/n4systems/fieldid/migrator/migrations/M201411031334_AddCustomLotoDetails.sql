DROP TABLE IF EXISTS loto_custom_details;

CREATE TABLE loto_custom_details (
  id bigint NOT NULL AUTO_INCREMENT,
  tenant_id BIGINT NOT NULL,
  created DATETIME NOT NULL,
  modified DATETIME NOT NULL,
  modifiedby BIGINT(21) DEFAULT NULL,
  createdby BIGINT(21) DEFAULT NULL,
  application_process VARCHAR(2500),
  removal_process VARCHAR(2500),
  testing_and_verification VARCHAR(2500),
  PRIMARY KEY (id)
);