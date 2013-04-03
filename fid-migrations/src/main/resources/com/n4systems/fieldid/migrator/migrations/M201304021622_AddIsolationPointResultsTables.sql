update procedures set workflow_state="OPEN";

alter table procedures add column asset_id bigint not null;
update procedures set asset_id = (select max(id) from assets);
alter table procedures add constraint fk_procedures_into_assets foreign key (asset_id) references assets (id);

alter table procedures modify column type_id bigint default null;

CREATE TABLE isolation_point_results (
  id bigint NOT NULL AUTO_INCREMENT,
  lock_asset_id bigint DEFAULT NULL,
  device_asset_id bigint DEFAULT NULL,
  location_check_time datetime DEFAULT NULL,
  method_check_time datetime DEFAULT NULL,
  lock_check_time datetime DEFAULT NULL,
  device_check_time datetime DEFAULT NULL,
  check_check_time datetime DEFAULT NULL,
  primary key(id),
  CONSTRAINT fk_isolation_point_results_lock_asset FOREIGN KEY (lock_asset_id) REFERENCES assets (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_isolation_point_results_device_asset FOREIGN KEY (device_asset_id) REFERENCES assets (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE procedures_lock_results (
  procedure_id bigint NOT NULL,
  isolation_point_result_id bigint NOT NULL,
  orderIdx bigint not null,
  PRIMARY KEY (procedure_id, isolation_point_result_id, orderIdx),
  CONSTRAINT fk_procedures_lock_results_procedures FOREIGN KEY (procedure_id) REFERENCES procedures (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_procedures_lock_results_isolation_point_results FOREIGN KEY (isolation_point_result_id) REFERENCES isolation_point_results (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE procedures_unlock_results (
  procedure_id bigint NOT NULL,
  isolation_point_result_id bigint NOT NULL,
  orderIdx bigint not null,
  PRIMARY KEY (procedure_id, isolation_point_result_id, orderIdx),
  CONSTRAINT fk_procedures_unlock_results_procedures FOREIGN KEY (procedure_id) REFERENCES procedures (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_procedures_unlock_results_isolation_point_results FOREIGN KEY (isolation_point_result_id) REFERENCES isolation_point_results (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

update column_mappings set path_expression="asset.identifier" where name = "procedure_search_identifier";
update column_mappings set path_expression="asset.rfidnumber" where name = "procedure_search_rfidnumber";
update column_mappings set path_expression="asset.customerRefNumber" where name = "procedure_search_referencenumber";
update column_mappings set path_expression="asset.identified" where name = "procedure_search_identified_date";
update column_mappings set path_expression="asset.type.group.name" where name = "procedure_search_assettypegroup";
update column_mappings set path_expression="asset.type.name" where name = "procedure_search_assettype";
update column_mappings set path_expression="asset.assetStatus.name" where name = "procedure_search_assetstatus";
update column_mappings set path_expression="asset.identifiedBy.fullName" where name = "procedure_search_identifiedby";
update column_mappings set path_expression="asset.description" where name = "procedure_search_asset_description";

alter table isolation_point_results add column isolation_point_id bigint not null;
alter table isolation_point_results add constraint fk_isolation_point_results_isolation_points foreign key (isolation_point_id) references isolation_points (id);

alter table isolation_points add column tenant_id bigint not null;
update isolation_points set tenant_id = (select id from tenants where name="n4");
alter table isolation_points add constraint fk_isolation_points_tenants foreign key (tenant_id) references tenants (id);