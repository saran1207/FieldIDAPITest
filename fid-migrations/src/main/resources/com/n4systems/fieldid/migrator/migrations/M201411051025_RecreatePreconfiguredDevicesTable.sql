DROP TABLE IF EXISTS preconfigured_devices;

CREATE TABLE preconfigured_devices (
  id bigint NOT NULL AUTO_INCREMENT,
  tenant_id BIGINT NOT NULL,
  created DATETIME NOT NULL,
  modified DATETIME NOT NULL,
  modifiedby BIGINT(21) DEFAULT NULL,
  createdby BIGINT(21) DEFAULT NULL,
  isolationPointSourceType VARCHAR(1),
  device VARCHAR(255) NOT NULL ,
  method VARCHAR(255),
  PRIMARY KEY (id)
);
