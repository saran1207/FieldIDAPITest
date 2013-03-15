CREATE TABLE isolation_device_descriptions (
  id bigint NOT NULL AUTO_INCREMENT,
  asset_type_id bigint DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_asset_types FOREIGN KEY (asset_type_id) REFERENCES assettypes (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE isolation_device_descriptions_attribute_values (
  isolation_device_description_id bigint NOT NULL,
  infooption_id bigint NOT NULL,
  orderIdx bigint not null,
  PRIMARY KEY (isolation_device_description_id, infooption_id, orderIdx),
  CONSTRAINT fk_isolation_device_description FOREIGN KEY (isolation_device_description_id) REFERENCES isolation_device_descriptions (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_infooption FOREIGN KEY (infooption_id) REFERENCES infooption (uniqueid) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE procedure_definitions (
  id bigint NOT NULL AUTO_INCREMENT,
  tenant_id bigint NOT NULL,
  procedure_code varchar(255) DEFAULT NULL,
  electronic_identifier varchar(255) NOT NULL,
  warnings varchar(1024) DEFAULT NULL,
  complete_points_in_order boolean NOT NULL,
  developed_by_id bigint NOT NULL,
  equipment_number varchar(255) NOT NULL,
  location varchar(255) DEFAULT NULL,
  predefinedlocation_id bigint DEFAULT NULL,
  equipment_description varchar(255) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_procedure_defs_locations FOREIGN KEY (predefinedlocation_id) REFERENCES predefinedlocations (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_procedure_defs_tenants FOREIGN KEY (tenant_id) REFERENCES tenants (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE isolation_points (
  id bigint NOT NULL AUTO_INCREMENT,
  identifier varchar(255) DEFAULT NULL,
  source varchar(255) DEFAULT NULL,
  device_definition_id bigint DEFAULT NULL,
  lock_definition_id bigint DEFAULT NULL,
  location varchar(255) DEFAULT NULL,
  method varchar(255) DEFAULT NULL,
  check_str varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_device FOREIGN KEY (device_definition_id) REFERENCES isolation_device_descriptions (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_lock FOREIGN KEY (lock_definition_id) REFERENCES isolation_device_descriptions (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE procedure_definitions_isolation_points (
  procedure_definition_id bigint NOT NULL,
  isolation_point_id bigint NOT NULL,
  orderIdx bigint not null,
  PRIMARY KEY (procedure_definition_id, isolation_point_id, orderIdx),
  CONSTRAINT fk_procedure FOREIGN KEY (procedure_definition_id) REFERENCES procedure_definitions (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_isolation_points FOREIGN KEY (isolation_point_id) REFERENCES tenants (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE procedure_definition_images (
  id bigint NOT NULL AUTO_INCREMENT,
  procedure_definition_id bigint NOT NULL,
  file_name varchar(1024) DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_procedures_defs FOREIGN KEY (procedure_definition_id) REFERENCES procedure_definitions (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE procedure_definitions_procedure_definition_images (
  procedure_definition_id bigint NOT NULL,
  procedure_definition_image_id bigint NOT NULL,
  orderIdx bigint not null,
  PRIMARY KEY (procedure_definition_id, procedure_definition_image_id, orderIdx),
  CONSTRAINT fk_procedures_defs_defs FOREIGN KEY (procedure_definition_id) REFERENCES procedure_definitions (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_procedures_def_images FOREIGN KEY (procedure_definition_image_id) REFERENCES procedure_definition_images (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE procedures (
  id bigint NOT NULL AUTO_INCREMENT,
  type_id bigint NOT NULL,
  assignee_id bigint DEFAULT NULL,
  performedby_id bigint DEFAULT NULL,
  latitude decimal(15,10) DEFAULT NULL,
  longitude decimal(15,10) DEFAULT NULL,
  primary key(id),
  CONSTRAINT fk_procedures_procedure_defs FOREIGN KEY (type_id) REFERENCES procedure_definitions (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_procedures_assignee FOREIGN KEY (assignee_id) REFERENCES users (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_procedures_performedby FOREIGN KEY (performedby_id) REFERENCES users (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);
