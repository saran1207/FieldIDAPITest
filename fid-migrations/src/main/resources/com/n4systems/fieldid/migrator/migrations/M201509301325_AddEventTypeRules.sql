CREATE TABLE event_type_rules (
  id bigint(21) NOT NULL AUTO_INCREMENT,
  tenant_id bigint(20) DEFAULT NULL,
  created datetime DEFAULT NULL,
  modified datetime DEFAULT NULL,
  modifiedby bigint(20) DEFAULT NULL,
  createdby bigint(20) DEFAULT NULL,
  eventtype_id bigint(20) DEFAULT NULL,
  event_result varchar(255) NOT NULL,
  assetstatus_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_event_type_rules_created_by FOREIGN KEY (createdby) REFERENCES users(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_event_type_rules_modified_by FOREIGN KEY (modifiedby) REFERENCES users(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_event_type_rules_event_type_id FOREIGN KEY (eventtype_id) REFERENCES eventtypes(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_event_type_rules_tenant_id FOREIGN KEY (tenant_id) REFERENCES tenants(id) ON DELETE NO ACTION ON UPDATE NO ACTION
)