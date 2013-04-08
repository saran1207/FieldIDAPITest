alter table image_annotation add column createdby bigint default null;
alter table image_annotation add column modifiedby bigint default null;

alter table image_annotation add column created datetime not null;
alter table image_annotation add column modified datetime not null;

alter table image_annotation add constraint fk_annotation_created_user foreign key (createdby) references users (id);
alter table image_annotation add constraint fk_annotation_modified_user foreign key (modifiedby) references users (id);

