alter table editable_images add tenant_id bigint(20);
alter table editable_images add constraint foreign key (tenant_id) references tenants(id);
alter table editable_images add thumbnail varchar(100);
alter table editable_images add created datetime;
alter table editable_images add modified datetime;
alter table editable_images add createdby bigint(20);
alter table editable_images add modifiedby bigint(20);
alter table editable_images add constraint fk_editable_images_createdby_users foreign key (createdby) references users (id);
alter table editable_images add constraint fk_editable_images_modifiedby_users foreign key (modifiedby) references users (id);


