CREATE TABLE IF NOT EXISTS loto_printouts (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  tenant_id bigint(20) NOT NULL,
  printout_name varchar(255) NOT NULL,
  printout_type varchar(10) NOT NULL,
  s3_path varchar(500) NOT NULL,
  selected tinyint(1) NOT NULL,
  created datetime,
  modified datetime,
  createdby bigint(20),
  modifiedby bigint(20),
  primary key(id),
  constraint fk_loto_printouts_createdby_users foreign key (createdby) references users(id),
  constraint fk_loto_printouts_modifiedby_users foreign key (modifiedby) references users(id),
  constraint fk_loto_printouts_tenant foreign key (tenant_id) references tenants(id),
  key loto_printouts_tenant_idx (tenant_id)
);
