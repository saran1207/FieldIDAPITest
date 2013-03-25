alter table image_annotation drop column type_id;
alter table image_annotation add column type varchar(20);
