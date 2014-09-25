ALTER TABLE `tenant_settings`
  ADD COLUMN `consumer_key` CHAR(32) NOT NULL,
  ADD COLUMN `consumer_secret` CHAR(32) NOT NULL;

UPDATE `tenant_settings` SET
  `consumer_key` = REPLACE(CAST(UUID() as char character set utf8),'-',''),
  `consumer_secret` = REPLACE(CAST(UUID() as char character set utf8),'-','');

ALTER TABLE `tenant_settings`
  ADD CONSTRAINT UNIQUE(`consumer_key`),
  ADD CONSTRAINT UNIQUE(`consumer_secret`);

ALTER TABLE `users`
  ADD COLUMN `token_key` CHAR(32) NOT NULL,
  ADD COLUMN `token_secret` CHAR(32) NOT NULL;

UPDATE `users` SET
  `token_key` = REPLACE(CAST(UUID() as char character set utf8),'-',''),
  `token_secret` = REPLACE(CAST(UUID() as char character set utf8),'-','');

ALTER TABLE `users`
  ADD CONSTRAINT UNIQUE(`token_key`),
  ADD CONSTRAINT UNIQUE(`token_secret`);
