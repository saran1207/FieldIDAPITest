CREATE TABLE widget_configurations_actions (
  id bigint NOT NULL AUTO_INCREMENT,
  org_id bigint DEFAULT NULL,
  user_id bigint DEFAULT NULL,
  action_type bigint DEFAULT NULL,
  date_range varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_actions_config_widget_orgs FOREIGN KEY (org_id) REFERENCES org_base (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_actions_config_widget_configs FOREIGN KEY (id) REFERENCES widget_configurations (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_actions_config_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_actions_event_type FOREIGN KEY (action_type) REFERENCES eventtypes (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);
