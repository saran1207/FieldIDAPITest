CREATE TABLE add_asset_history (
  id bigint NOT NULL AUTO_INCREMENT,
  user_id bigint DEFAULT NULL,
  assigned_user_id bigint DEFAULT NULL,
  asset_type_id bigint DEFAULT NULL,
  asset_status_id bigint DEFAULT NULL,
  purchase_order varchar(255) DEFAULT NULL,
  location varchar(255) DEFAULT NULL,
  owner_id bigint DEFAULT NULL,
  tenant_id bigint DEFAULT NULL,
  predefinedlocation_id bigint DEFAULT NULL,
  createdby bigint DEFAULT NULL,
  modifiedby bigint DEFAULT NULL,
  created datetime DEFAULT NULL,
  modified datetime DEFAULT NULL,
  PRIMARY KEY (id),
  key index_add_asset_history_assigned_user (assigned_user_id),
  key index_add_asset_history_asset_type (asset_type_id),
  key index_add_asset_history_owner (owner_id),
  key index_add_asset_history_tenant (tenant_id),
  key index_add_asset_history_predef_location (predefinedlocation_id),
  key index_add_asset_history_asset_status (asset_status_id),
  CONSTRAINT fk_add_asset_history_asset_type FOREIGN KEY (asset_type_id) REFERENCES assettypes (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_add_asset_history_asset_status FOREIGN KEY (asset_status_id) REFERENCES assetstatus (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_add_asset_history_owner FOREIGN KEY (owner_id) REFERENCES org_base (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_add_asset_history_assigned_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_add_asset_history_user FOREIGN KEY (assigned_user_id) REFERENCES users (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_add_asset_history_created_user FOREIGN KEY (createdby) REFERENCES users (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_add_asset_history_modified_user FOREIGN KEY (modifiedby) REFERENCES users (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_add_asset_history_predefined_location FOREIGN KEY (predefinedlocation_id) REFERENCES predefinedlocations (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

insert into add_asset_history (id,user_id,assigned_user_id,asset_type_id,asset_status_id,purchase_order,location,owner_id,tenant_id,predefinedlocation_id) (select uniqueid,r_fieldiduser,assigneduser_id,r_producttype,r_productstatus,purchaseorder,location,owner_id,tenant_id,predefinedlocation_id from addassethistory);

CREATE TABLE add_asset_history_infooption (
  id bigint(21) NOT NULL AUTO_INCREMENT,
  add_asset_history_id bigint(20) NOT NULL,
  infooption_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_add_asset_history_infooption_add_asset_history FOREIGN KEY (add_asset_history_id) REFERENCES add_asset_history (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_add_asset_history_infooption_infooption FOREIGN KEY (infooption_id) REFERENCES infooption (uniqueid) ON DELETE NO ACTION ON UPDATE NO ACTION
);

insert into add_asset_history_infooption (add_asset_history_id,infooption_id) (select r_addproducthistory,r_infooption from addassethistory_infooption);

alter table identifier_counters add constraint fk_identifier_counter_created foreign key (createdby) references users (id);
alter table identifier_counters add constraint fk_identifier_counter_modified foreign key (modifiedby) references users (id);



