ALTER TABLE tenant_languages
    DROP FOREIGN KEY  fk_tenant_language_tenant,
    DROP PRIMARY KEY,
    ADD PRIMARY KEY (tenant_settings_id, language, orderidx);

ALTER TABLE tenant_languages
    ADD CONSTRAINT fk_tenant_language_tenant FOREIGN KEY (tenant_settings_id) REFERENCES tenant_settings(id);