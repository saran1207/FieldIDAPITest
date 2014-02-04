
CREATE TABLE recurring_place_events (
  id bigint(21) NOT NULL AUTO_INCREMENT,
  tenant_id bigint(21) NOT NULL,
  created datetime NOT NULL,
  modified datetime NOT NULL,
  modifiedby bigint(21) DEFAULT NULL,
  createdby bigint(21) DEFAULT NULL,
  state varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  event_type_id bigint(20) NOT NULL,
  owner_id bigint(21) NOT NULL,
  recurrence_id bigint(20) NOT NULL,
  autoassign tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (id),
  KEY idx_recurring_place_events_tenant (tenant_id),
  KEY idx_recurring_place_events_eventtype (event_type_id),
  KEY idx_recurring_place_events_owner (owner_id),
  CONSTRAINT fk_recurring_place_events_owner FOREIGN KEY (owner_id) REFERENCES org_base(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_recurring_place_events_eventtype FOREIGN KEY (event_type_id) REFERENCES place_event_types(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_recurring_place_events_tenant FOREIGN KEY (tenant_id) REFERENCES tenants (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_recurring_place_event_recurrence FOREIGN KEY (recurrence_id) REFERENCES recurrence (id)
);
