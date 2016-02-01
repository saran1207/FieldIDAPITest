-- Remove the certify permission on all FULL users
UPDATE users u INNER JOIN tenant_settings s ON u.tenant_id = s.tenant_id SET u.permissions = u.permissions - 1024
WHERE s.loto_enabled = true AND u.permissions <> 2147483647 AND u.usertype = 'FULL';

-- Restore the certify permission on all FULL users set as the Certifier in Tenant Settings
UPDATE users INNER JOIN tenant_settings ON users.id = approval_user_id SET u.permissions = u.permissions + 1024
WHERE loto_enabled = true AND usertype = 'FULL' AND state ='ACTIVE' AND u.permissions <> 2147483647;

-- Restore the certify permission on all FULL users in the Certifier Group in Tenant Settings
UPDATE tenant_settings s INNER JOIN users_user_groups g ON approval_user_group_id = user_group_id INNER JOIN users u ON u.id = g.user_id SET u.permissions = u.permissions + 1024
WHERE s.loto_enabled = true AND u.state ='ACTIVE' AND u.usertype ='FULL' AND u.permissions <> 2147483647;