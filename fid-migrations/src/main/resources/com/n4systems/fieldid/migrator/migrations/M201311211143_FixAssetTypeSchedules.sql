CREATE TABLE asset_type_schedules (
  id bigint(21) NOT NULL AUTO_INCREMENT,
  tenant_id bigint(20) DEFAULT NULL,
  created datetime NOT NULL,
  modified datetime NOT NULL,
  modifiedby bigint(20) DEFAULT NULL,
  frequency bigint(20) DEFAULT NULL,
  asset_type_id bigint(20) NOT NULL,
  autoschedule tinyint(1) DEFAULT NULL,
  thing_event_type_id bigint(20) DEFAULT NULL,
  owner_id bigint(20) DEFAULT NULL,
  createdby bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY index_asset_type_schedules_on_modifiedby (modifiedby),
  KEY index_asset_type_schedules_on_tenant (tenant_id),
  KEY fk_asset_type_schedules_owner (owner_id),
  KEY index_asset_type_schedules_on_asset_id (asset_type_id),
  KEY index_asset_type_schedules_on_thing_event_type_id (thing_event_type_id),
  KEY fk_asset_type_schedules_created_by_user(createdby),
  CONSTRAINT fk_asset_type_schedules_asset_types FOREIGN KEY (asset_type_id) REFERENCES assettypes (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_asset_type_schedules_event_types FOREIGN KEY (thing_event_type_id) REFERENCES eventtypes (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_asset_type_schedules_createdby FOREIGN KEY (createdby) REFERENCES users (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_asset_type_schedules_owner_id FOREIGN KEY (owner_id) REFERENCES org_base (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_asset_type_schedules_modified_user FOREIGN KEY (modifiedby) REFERENCES users (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

insert into asset_type_schedules(select id,tenant_id,created,modified,modifiedby,frequency,assettype_id,autoschedule,eventtype_id,owner_id,createdby from assettypeschedules);
