alter table image_annotation add column tenant_id bigint;
alter table image_annotation add constraint fk_image_annotation_tenant foreign key (tenant_id) references tenants(id);
alter table image_annotation drop column direction;

alter table image_annotation add column createdby bigint default null;
alter table image_annotation add column modifiedby bigint default null;

alter table image_annotation add column created datetime not null;
alter table image_annotation add column modified datetime not null;

alter table image_annotation add constraint fk_annotation_created_user foreign key (createdby) references users (id);
alter table image_annotation add constraint fk_annotation_modified_user foreign key (modifiedby) references users (id);

