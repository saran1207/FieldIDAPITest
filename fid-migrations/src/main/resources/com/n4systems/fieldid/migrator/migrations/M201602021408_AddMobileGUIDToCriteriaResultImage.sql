alter table criteriaresult_images add column mobileguid varchar(255) not null;
update criteriaresult_images set mobileguid = uuid();

alter table criteriaresult_images add constraint unique_mobile_guid unique key (mobileguid);