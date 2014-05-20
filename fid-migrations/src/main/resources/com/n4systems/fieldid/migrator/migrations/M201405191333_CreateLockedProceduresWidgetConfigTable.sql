CREATE TABLE `widget_configurations_lockedout_procedures` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_configs_lockedout_procedures_widget_orgs` (`org_id`),
  CONSTRAINT `fk_lockedout_procedures_config_widget_configs` FOREIGN KEY (`id`) REFERENCES `widget_configurations` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_configs_lockedout_procedures_widget_orgs` FOREIGN KEY (`org_id`) REFERENCES `org_base` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);