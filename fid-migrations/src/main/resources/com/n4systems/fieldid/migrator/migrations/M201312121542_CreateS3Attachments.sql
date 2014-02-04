drop table s3_attachments;

create table s3_attachments (
  id bigint(20) not null auto_increment,
  tenant_id bigint(20) not null,
  path varchar(500) not null,
  type varchar(50) not null,
  content_type varchar(50) not null,
  md5_sum varchar(100) not null,
  comments varchar(2000),
  created datetime,
  modified datetime,
  createdby bigint(20),
  modifiedby bigint(20),
  primary key(id),
  constraint fk_s3_attachment_createdby_users foreign key (createdby) references users (id),
  constraint fk_s3_attachment_modifiedby_users foreign key (modifiedby) references users (id),
  constraint fk_s3_attachment_tenant foreign key (tenant_id) references tenants(id),
  key s3_attachments_tenant_idx (tenant_id),
  key s3_attachments_name_idx (path)
);