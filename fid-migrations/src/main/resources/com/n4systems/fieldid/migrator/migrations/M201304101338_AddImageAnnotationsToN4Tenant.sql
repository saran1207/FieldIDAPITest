set @n4_tenant = (select id from tenants where name='n4');
set @first_user_on_n4 = (select id from users where tenant_id = @n4_tenant and state="ACTIVE" and usertype="FULL" limit 1);
set @published=(select min(id) from procedure_definitions where published_state='PUBLISHED' and tenant_id=@n4_tenant);
set @draft=(select min(id) from procedure_definitions where published_state='DRAFT' and tenant_id=@n4_tenant);

insert into editable_images (filename,tenant_id,created, modified, createdby, modifiedby) values('tenants/15511493/foo/bar/stuff/portrait.png.medium', @n4_tenant, curdate(), curdate(), @first_user_on_n4, @first_user_on_n4);
set @portrait = (select max(id) from editable_images where filename='tenants/15511493/foo/bar/stuff/portrait.png.medium');
insert into editable_images (filename,tenant_id,created, modified, createdby, modifiedby) values('tenants/15511493/foo/bar/stuff/wide.png.medium', @n4_tenant, curdate(), curdate(), @first_user_on_n4, @first_user_on_n4);
set @wide = (select max(id) from editable_images where filename='tenants/15511493/foo/bar/stuff/wide.png.medium');

insert image_annotation (text, image_id, x, y, type, tenant_id, createdby, modifiedby, created, modified) values('portrait',@portrait, .1, .75, 'W', @n4_tenant, @first_user_on_n4, @first_user_on_n4, curdate(), curdate() );
insert image_annotation (text, image_id, x, y, type, tenant_id, createdby, modifiedby, created, modified) values('wide',@wide, .4, .35, 'W', @n4_tenant, @first_user_on_n4, @first_user_on_n4, curdate(), curdate() );

insert into procedure_definition_images (id, procedure_definition_id) values(@portrait, @published);
insert into procedure_definition_images (id, procedure_definition_id) values(@wide, @draft);
