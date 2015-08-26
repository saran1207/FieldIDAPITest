DROP TABLE IF EXISTS escalation_rule_event_linker;

drop table if exists escalation_rule_execution_queue;

CREATE TABLE IF NOT EXISTS escalation_rule_execution_queue (
  id BIGINT(21) NOT NULL AUTO_INCREMENT,
  event_id BIGINT(20) NOT NULL,
  rule_id BIGINT(20) NOT NULL,
  rule_has_run BIGINT(1) NOT NULL DEFAULT 0,
  notify_date DATETIME NOT NULL,
  event_mod_date DATETIME NOT NULL,
  map_json VARCHAR(5000) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_escalation_rule_execution_queue_event FOREIGN KEY (event_id) REFERENCES events(id),
  CONSTRAINT fk_escalation_rule_execution_queue_rule FOREIGN KEY (rule_id) REFERENCES assignment_escalation_rules(id)
)