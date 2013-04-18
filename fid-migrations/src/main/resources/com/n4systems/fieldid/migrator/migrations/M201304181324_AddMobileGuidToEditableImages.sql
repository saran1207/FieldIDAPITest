alter table editable_images add column mobileguid varchar(255) not null;
update editable_images set mobileguid = uuid();