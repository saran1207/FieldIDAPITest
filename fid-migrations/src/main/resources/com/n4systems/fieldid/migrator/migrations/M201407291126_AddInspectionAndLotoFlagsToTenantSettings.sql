ALTER TABLE tenant_settings ADD COLUMN inspections_enabled TINYINT(1) NOT NULL;
ALTER TABLE tenant_settings ADD COLUMN loto_enabled TINYINT(1) NOT NULL;

UPDATE tenant_settings SET inspections_enabled = true;

UPDATE tenant_settings SET loto_enabled = true WHERE tenant_id IN (
  SELECT tenant_id
  FROM org_base b, org_primary p, org_extendedfeatures e
  WHERE b.id = p.org_id AND p.org_id = e.org_id AND feature = 'LotoProcedures');
