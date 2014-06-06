CREATE TABLE recurring_loto_events (
  id bigint(21) NOT NULL AUTO_INCREMENT,
  tenant_id bigint(21) NOT NULL,
  created datetime NOT NULL,
  modified datetime NOT NULL,
  modifiedby bigint(21) DEFAULT NULL,
  createdby bigint(21) DEFAULT NULL,
  state varchar(255) NOT NULL,
  procedure_definition_id bigint(20) NOT NULL,
  recurrence_id bigint(20) NOT NULL,
  assignee_id bigint(20) DEFAULT NULL,
  assigned_group_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY idx_recurring_loto_tenant (tenant_id),
  KEY idx_recurring_loto_proceduredef (procedure_definition_id),
  KEY idx_recurring_loto_assignee (assignee_id),
  CONSTRAINT fk_recurring_loto_proceduredef FOREIGN KEY (procedure_definition_id) REFERENCES procedure_definitions(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_recurring_loto_tenant FOREIGN KEY (tenant_id) REFERENCES tenants (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_recurring_loto_recurrence FOREIGN KEY (recurrence_id) REFERENCES recurrence (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_recurring_loto_assignee FOREIGN KEY (assignee_id) REFERENCES users (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_recurring_loto_assigned_group FOREIGN KEY (assigned_group_id) REFERENCES user_groups (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

ALTER TABLE procedures ADD COLUMN recurring_event_id BIGINT(21);
ALTER TABLE procedures ADD CONSTRAINT fk_events_recurring_loto_events FOREIGN KEY (recurring_event_id) REFERENCES recurring_loto_events(id) ON UPDATE NO ACTION ON DELETE NO ACTION;