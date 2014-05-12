CREATE TABLE widget_configurations_upcoming_loto (
  id BIGINT(21) NOT NULL AUTO_INCREMENT,
  date_range VARCHAR(255) DEFAULT NULL,
  period BIGINT(20) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_upcoming_loto_config_widget_configs FOREIGN KEY (id) REFERENCES widget_configurations (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);