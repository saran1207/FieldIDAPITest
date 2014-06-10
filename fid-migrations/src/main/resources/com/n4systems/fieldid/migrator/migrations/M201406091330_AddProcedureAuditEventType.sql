CREATE TABLE procedure_audit_event_types (
  id bigint NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_procedure_audit_event_types_on_event_types FOREIGN KEY (id) REFERENCES eventtypes (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);


 CREATE TABLE procedure_audit_events (
  id bigint(20) NOT NULL,
  procedure_definition_id bigint(20) NOT NULL,
  recurring_event_id bigint(21) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY fk_procedure_audit_events_on_place (procedure_definition_id),
  KEY fk_events_recurring_procedure_audit_events (recurring_event_id),
  CONSTRAINT fk_events_recurring_procedure_audit_events FOREIGN KEY (recurring_event_id) REFERENCES recurring_loto_events (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_procedure_audit_events_on_events FOREIGN KEY (id) REFERENCES events (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_procedure_audit_events_on_procedure_definition FOREIGN KEY (procedure_definition_id) REFERENCES procedure_definitions (id)
);

ALTER TABLE recurring_loto_events ADD COLUMN type varchar(255) NOT NULL;

UPDATE recurring_loto_events SET type = 'LOTO';