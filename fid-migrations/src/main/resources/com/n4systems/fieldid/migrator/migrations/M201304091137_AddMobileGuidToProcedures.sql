alter table procedures add column mobileguid varchar(255) not null;
update procedures set mobileguid = uuid();

alter table procedures add constraint unique_mobile_guid unique key (mobileguid);