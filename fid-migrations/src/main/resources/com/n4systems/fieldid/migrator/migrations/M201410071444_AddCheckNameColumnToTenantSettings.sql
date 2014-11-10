alter table tenant_settings ADD (
  checkName tinyint(1)
);

update tenant_settings set checkName=0;