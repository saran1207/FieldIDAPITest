update image_annotation set type='W' where type is null;
alter table image_annotation modify type varchar(25) not null; 
