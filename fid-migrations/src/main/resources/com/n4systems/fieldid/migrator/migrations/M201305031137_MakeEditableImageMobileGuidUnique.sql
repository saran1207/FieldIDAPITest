update editable_images set mobileguid = uuid();
alter table editable_images modify column mobileguid varchar(255) not null unique;