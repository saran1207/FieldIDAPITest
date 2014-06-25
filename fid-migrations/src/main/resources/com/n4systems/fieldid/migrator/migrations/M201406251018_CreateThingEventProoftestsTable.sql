CREATE TABLE `thing_event_prooftests` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `thing_event_id` bigint(20) NOT NULL,
  `prooftesttype` varchar(255) DEFAULT NULL,
  `peakload` varchar(255) DEFAULT NULL,
  `duration` varchar(255) DEFAULT NULL,
  `peakloadduration` varchar(255) DEFAULT NULL,
  `prooftestdata` mediumtext DEFAULT NULL,
  `prooftestfilename` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_prooftests_on_thing_event` (`thing_event_id`),
  CONSTRAINT `fk_prooftests_on_thing_event` FOREIGN KEY (`thing_event_id`) REFERENCES `thing_events` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO `thing_event_prooftests`
(`thing_event_id`,`prooftesttype`,`peakload`,`duration`,`peakloadduration`)
SELECT `id`,`prooftesttype`, `peakload`, `duration`, `peakloadduration`
FROM thing_events
WHERE prooftesttype IS NOT NULL;
