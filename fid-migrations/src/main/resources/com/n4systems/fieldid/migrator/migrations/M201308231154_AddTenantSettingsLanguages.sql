create table tenant_languages (
  tenant_settings_id bigint,
  language varchar(10),
  orderidx bigint,
  PRIMARY KEY (tenant_settings_id, language),
  CONSTRAINT fk_tenant_language_tenant FOREIGN KEY (tenant_settings_id) REFERENCES tenant_settings (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

insert into tenant_languages values( (select ts.id from tenant_settings ts join tenants t on ts.tenant_id = t.id where t.name='n4'), 'fr', 0);
insert into tenant_languages values( (select ts.id from tenant_settings ts join tenants t on ts.tenant_id = t.id where t.name='n4'), 'de', 1);
insert into tenant_languages values( (select ts.id from tenant_settings ts join tenants t on ts.tenant_id = t.id where t.name='n4'), 'it', 2);
