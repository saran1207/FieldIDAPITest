ALTER TABLE recurring_loto_events ADD COLUMN audit_event_id bigint(21) DEFAULT NULL;
ALTER TABLE recurring_loto_events ADD CONSTRAINT fk_procedure_audit_event_type_on_recurring_loto_events FOREIGN KEY (audit_event_id) REFERENCES procedure_audit_event_types(id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE procedure_audit_events DROP FOREIGN KEY fk_procedure_audit_events_on_procedure_definition;
ALTER TABLE procedure_audit_events DROP KEY fk_procedure_audit_events_on_place;
ALTER TABLE procedure_audit_events ADD CONSTRAINT fk_procedure_audit_events_on_procedure_definition FOREIGN KEY (procedure_definition_id) REFERENCES procedure_definitions (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

