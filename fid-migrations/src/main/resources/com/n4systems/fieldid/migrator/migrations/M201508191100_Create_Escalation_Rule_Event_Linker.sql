DROP TABLE IF EXISTS escalation_rule_asset_linker;
DROP TABLE IF EXISTS escalation_rule_event_linker;

CREATE TABLE IF NOT EXISTS escalation_rule_event_linker (
  id bigint(21) NOT NULL AUTO_INCREMENT,
  event_id bigint(20) NOT NULL,
  rule_id bigint(20) NOT NULL,
  rule_has_run tinyint(1) NOT NULL DEFAULT 0,
  primary key(id),
  constraint fk_escalation_rule_event_linker_event foreign key (event_id) references masterevents(event_id),
  constraint fk_escalation_rule_event_linker_rule foreign key (rule_id) references assignment_escalation_rules(id)
);