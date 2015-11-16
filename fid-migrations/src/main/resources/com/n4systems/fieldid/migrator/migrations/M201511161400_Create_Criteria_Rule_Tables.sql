DROP TABLE IF EXISTS one_click_rules;
DROP TABLE IF EXISTS select_rules;
DROP TABLE IF EXISTS number_field_rules;
DROP TABLE IF EXISTS criteria_rules;

CREATE TABLE IF NOT EXISTS criteria_rules (
  id BIGINT(21) NOT NULL AUTO_INCREMENT,
  action VARCHAR(20) NOT NULL,
  criteria_id BIGINT(21) NOT NULL,
  tenant_id BIGINT(21) NOT NULL,
  created DATETIME NOT NULL,
  createdby BIGINT(21) NOT NULL,
  modified DATETIME,
  modifiedby BIGINT(21),
  PRIMARY KEY(id),
  CONSTRAINT fk_criteria_rules_to_criteria FOREIGN KEY (criteria_id) REFERENCES criteria(id)
);

CREATE TABLE IF NOT EXISTS one_click_rules (
  id BIGINT(21) NOT NULL,
  button_id BIGINT(21) NOT NULL,
  PRIMARY KEY(id),
  CONSTRAINT fk_one_click_rules_to_criteria_rules FOREIGN KEY (id) REFERENCES criteria_rules(id),
  CONSTRAINT fk_one_click_rules_to_buttons FOREIGN KEY (button_id) REFERENCES buttons(id)
);

CREATE TABLE IF NOT EXISTS select_rules (
  id BIGINT(21) NOT NULL,
  select_value VARCHAR(255) NOT NULL,
  PRIMARY KEY(id),
  CONSTRAINT fk_select_rules_to_criteria_rules FOREIGN KEY (id) REFERENCES criteria_rules(id)
);

CREATE TABLE IF NOT EXISTS number_field_rules (
  id BIGINT(21) NOT NULL,
  number_1 DECIMAL(65,10) NOT NULL,
  number_2 DECIMAL(65, 10),
  comparison_type VARCHAR(2) NOT NULL,
  CONSTRAINT fk_number_field_rules_to_criteria_rules FOREIGN KEY (id) REFERENCES criteria_rules(id)
);