ALTER TABLE tenant_settings ADD usageBasedUsersEnabled TINYINT(1) NOT NULL AFTER maxReadonlyUsers;

ALTER TABLE tenant_settings ADD usageBasedUserEvents BIGINT(20) NOT NULL AFTER usageBasedUsersEnabled;

UPDATE tenant_settings SET usageBasedUsersEnabled = false, usageBasedUserEvents = 0;