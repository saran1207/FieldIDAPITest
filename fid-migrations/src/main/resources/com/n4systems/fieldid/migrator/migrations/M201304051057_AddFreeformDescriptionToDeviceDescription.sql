alter table isolation_device_descriptions add column freeform_description varchar(512) default null;

update isolation_points set source="W";
alter table isolation_points modify column source varchar(255) not null;