CREATE TABLE widget_configurations_procedures_published (
  id bigint(21) NOT NULL AUTO_INCREMENT,
  date_range varchar(255) DEFAULT NULL,
  granularity varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_procedures_published_config_widget_configs` FOREIGN KEY (`id`) REFERENCES `widget_configurations` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);