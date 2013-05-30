ALTER TABLE tenant_settings ADD unlimitedUsersEnabled TINYINT(1) NOT NULL AFTER maxReadonlyUsers;

ALTER TABLE tenant_settings ADD unlimitedUserEvents BIGINT(20) NOT NULL AFTER unlimitedUsersEnabled;

UPDATE tenant_settings SET unlimitedUsersEnabled = false, unlimitedUserEvents = 0;