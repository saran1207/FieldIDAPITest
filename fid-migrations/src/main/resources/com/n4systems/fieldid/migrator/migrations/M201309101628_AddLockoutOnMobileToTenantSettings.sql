ALTER TABLE tenant_settings ADD COLUMN lockoutOnMobile TINYINT(1) DEFAULT 0;
UPDATE tenant_settings set lockoutOnMobile = 0;