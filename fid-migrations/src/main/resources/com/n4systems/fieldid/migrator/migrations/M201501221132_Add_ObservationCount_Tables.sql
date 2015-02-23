/*
This table is for the base entity
*/
CREATE TABLE IF NOT EXISTS observationcount (
  id bigint(21) NOT NULL AUTO_INCREMENT,
  created datetime NOT NULL,
  modified datetime NOT NULL,
  modifiedby bigint(20) DEFAULT NULL,
  createdby bigint(20) DEFAULT NULL,
  tenant_id bigint(20) NOT NULL,
  name varchar(1024) DEFAULT NULL,
  value decimal(15,10) DEFAULT NULL,
  counted tinyint(1) DEFAULT NULL,
  state varchar(255) NOT NULL,
  PRIMARY KEY (id)
);

/*
This table is for the groups
*/
CREATE TABLE IF NOT EXISTS observationcount_groups (
  id bigint(21) NOT NULL AUTO_INCREMENT,
  created datetime NOT NULL,
  modified datetime NOT NULL,
  modifiedby bigint(20) DEFAULT NULL,
  createdby bigint(20) DEFAULT NULL,
  tenant_id bigint(20) NOT NULL,
  name varchar(1024) DEFAULT NULL,
  state varchar(255) NOT NULL,
  PRIMARY KEY (id)
);

/*
This is the lookup table for the above two tables.
*/
CREATE TABLE IF NOT EXISTS observationcount_groups_observationcounts (
  id bigint(21) NOT NULL AUTO_INCREMENT,
  observationcount_id bigint(20) NOT NULL,
  observationcount_group_id bigint(20) NOT NULL,
  orderIdx bigint(20) NOT NULL,
  PRIMARY KEY (id),
  KEY fk_groups_observationcount (observationcount_id),
  KEY fk_groups_observationcount_group (observationcount_group_id),
  CONSTRAINT fk_groups_observationcount_group FOREIGN KEY (observationcount_group_id) REFERENCES observationcount_groups (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_groups_observationcount FOREIGN KEY (observationcount_id) REFERENCES observationcount (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

/*
This is the for the criteria
*/
CREATE TABLE IF NOT EXISTS observationcount_criteria (
  id bigint(21) NOT NULL AUTO_INCREMENT,
  observationcount_group_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  KEY fk_observationcount_criteria_to_groups (observationcount_group_id),
  CONSTRAINT fk_observationcount_criteria_to_groups FOREIGN KEY (observationcount_group_id) REFERENCES observationcount_groups (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

/*
This is the for the criteria results
*/
CREATE TABLE IF NOT EXISTS observationcount_criteriaresults (
  id bigint(21) NOT NULL AUTO_INCREMENT,
  observationcount_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  KEY fk_observationcount_results_to_observationcount (observationcount_id),
  CONSTRAINT fk_observationcount_results_to_observationcount FOREIGN KEY (observationcount_id) REFERENCES observationcount (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);
