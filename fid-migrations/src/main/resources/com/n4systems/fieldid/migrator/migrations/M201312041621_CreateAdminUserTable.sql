CREATE TABLE `admin_users` (
  `id`        BIGINT(21) NOT NULL AUTO_INCREMENT,
  `enabled`   BIT(1) NOT NULL DEFAULT 1,
  `type`      VARCHAR(48) NOT NULL,
  `email`     VARCHAR(255) NOT NULL,
  `password`  CHAR(128) NOT NULL,
  `salt`      CHAR(128) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY `admin_users_email` (`email`)
);