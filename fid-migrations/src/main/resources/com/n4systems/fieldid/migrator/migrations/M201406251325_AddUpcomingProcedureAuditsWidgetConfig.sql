CREATE TABLE widget_configurations_upcoming_procedure_audits (
  id BIGINT(21) NOT NULL AUTO_INCREMENT,
  org_id BIGINT(20) DEFAULT NULL,
  date_range VARCHAR(255) DEFAULT NULL,
  period BIGINT(20) NOT NULL,
  PRIMARY KEY (id),
  KEY fk_upcoming_procedure_audits_config_widget_orgs (org_id),
  CONSTRAINT fk_upcoming_procedure_audits_config_widget_configs FOREIGN KEY (id) REFERENCES widget_configurations (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_upcoming_procedure_audits_config_widget_orgs FOREIGN KEY (org_id) REFERENCES org_base (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);