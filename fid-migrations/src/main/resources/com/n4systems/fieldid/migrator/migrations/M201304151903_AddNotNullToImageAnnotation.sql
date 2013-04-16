alter table image_annotation modify tenant_id bigint not null;
alter table image_annotation modify createdby bigint not null;
alter table image_annotation modify modifiedby bigint not null;
alter table image_annotation modify x float not null;
alter table image_annotation modify y float not null;

