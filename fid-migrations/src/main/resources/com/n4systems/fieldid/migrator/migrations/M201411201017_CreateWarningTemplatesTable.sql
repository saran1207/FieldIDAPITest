CREATE TABLE IF NOT EXISTS warning_templates (
	id bigint(20) NOT NULL AUTO_INCREMENT,
	tenant_id bigint(20) NOT NULL,
	name varchar(50) NOT NULL,
	warning varchar(255) NOT NULL,
	created datetime NOT NULL,
	createdby bigint(20) NOT NULL,
	modified datetime,
	modifiedby bigint(20),
	primary key(id),
	constraint fk_warning_templates_createdby_users foreign key (createdby) references users(id),
	constraint fk_warning_templates_modifiedby_users foreign key (modifiedby) references users(id),
	constraint fk_warning_templates_tenant foreign key (tenant_id) references tenants(id)
);