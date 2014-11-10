CREATE TABLE `request_log` (
  `consumer_key`    CHAR(32) NOT NULL,
  `consumer_secret` CHAR(32) NOT NULL,
  `nonce`           CHAR(36) NOT NULL,
  `timestamp`       BIGINT   NOT NULL,
  PRIMARY KEY (`consumer_key`, `consumer_secret`, `nonce`, `timestamp`)
);