create table user_groups

(id bigint(20) not null auto_increment,
created datetime,
modified datetime,
createdby bigint(20),
modifiedby bigint(20),
name varchar(255) not null,
group_id varchar(255),
state varchar(255) not null,
tenant_id bigint(20) not null,
primary key (id),
key tenant_index (tenant_id),
key name_index (name),
key group_id_index (group_id),
constraint fk_createdby_users foreign key (createdby) references users (id),
constraint fk_modifiedby_users foreign key (modifiedby) references users (id),
constraint fk_tenant_id_tenants foreign key (tenant_id) references tenants (id));