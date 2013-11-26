CREATE TABLE associated_event_types (
  id bigint(21) NOT NULL AUTO_INCREMENT,
  created datetime NOT NULL,
  modified datetime NOT NULL,
  modifiedby bigint(20) DEFAULT NULL,
  thing_event_type_id bigint(20) DEFAULT NULL,
  asset_type_id bigint(20) NOT NULL,
  tenant_id bigint(20) DEFAULT NULL,
  createdby bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY idx_associated_event_types_unique (asset_type_id,thing_event_type_id),
  KEY fk_associated_event_types_users (modifiedby),
  KEY fk_associated_event_types_tenants (tenant_id),
  KEY index_associated_event_types_on_thing_event_type_id (thing_event_type_id),
  KEY fk_associated_event_types_created_by_user (createdby),
  CONSTRAINT associated_event_types_thing_events FOREIGN KEY (thing_event_type_id) REFERENCES thing_event_types (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT associated_event_types_created_user FOREIGN KEY (createdby) REFERENCES users (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_associated_event_types_asset_types FOREIGN KEY (asset_type_id) REFERENCES assettypes (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_associated_event_types_tenants FOREIGN KEY (tenant_id) REFERENCES tenants (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_associated_event_types_users FOREIGN KEY (modifiedby) REFERENCES users (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);


insert into associated_event_types (select id,created,modified,modifiedby,eventtype_id,producttype_id,tenant_id,createdby from associatedeventtypes);