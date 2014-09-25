ALTER TABLE `tenant_settings` ADD COLUMN `public_api_enabled` tinyint(1) DEFAULT '0';
UPDATE `tenant_settings` SET `public_api_enabled` = 0;
