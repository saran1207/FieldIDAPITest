UPDATE users u INNER JOIN tenant_settings s ON u.tenant_id = s.tenant_id SET u.permissions = u.permissions + 130560
WHERE s.loto_enabled = true and u.permissions <> 2147483647 and u.usertype = 'FULL';

UPDATE users u INNER JOIN tenant_settings s ON u.tenant_id = s.tenant_id SET u.permissions = u.permissions + 58880
WHERE s.loto_enabled = true and u.permissions <> 2147483647 and u.usertype = 'LITE';

UPDATE users u INNER JOIN tenant_settings s ON u.tenant_id = s.tenant_id SET u.permissions = u.permissions + 58880
WHERE s.loto_enabled = true and u.permissions <> 2147483647 and u.usertype = 'USAGE_BASED';

UPDATE users u INNER JOIN tenant_settings s ON u.tenant_id = s.tenant_id SET u.permissions = u.permissions + 16384
WHERE s.loto_enabled = true and u.permissions <> 2147483647 and u.usertype = 'READONLY';

