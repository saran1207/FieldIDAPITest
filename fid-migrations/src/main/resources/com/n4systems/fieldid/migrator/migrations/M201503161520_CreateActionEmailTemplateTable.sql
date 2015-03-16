CREATE TABLE IF NOT EXISTS action_email_customization (
	id bigint(20) NOT NULL AUTO_INCREMENT,
	tenant_id bigint(20) NOT NULL,
	email_subject varchar(255),
	sub_heading varchar(2048),
	created datetime NOT NULL,
	createdby bigint(20) NOT NULL,
	modified datetime,
	modifiedby bigint(20),
	primary key(id),
	constraint fk_action_email_customization_createdby_users foreign key (createdby) references users(id),
	constraint fk_action_email_customization_modifiedby_users foreign key (modifiedby) references users(id),
	constraint fk_action_email_customization_tenant foreign key (tenant_id) references tenants(id)
);
