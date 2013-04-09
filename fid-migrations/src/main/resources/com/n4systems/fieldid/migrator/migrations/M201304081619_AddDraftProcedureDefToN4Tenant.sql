set @n4_tenant = (select id from tenants where name="n4");
set @chain_sling_asset = (select id from assettypes where tenant_id = @n4_tenant and name = "Chain Sling" );
set @first_chain_sling_id = (select id from assets where type_id = @chain_sling_asset and state="ACTIVE" limit 1);
set @first_user_on_n4 = (select id from users where tenant_id = @n4_tenant and state="ACTIVE" and usertype="FULL" limit 1);

insert into procedure_definitions (tenant_id, procedure_code, electronic_identifier, warnings, complete_points_in_order, developed_by_id, equipment_description, asset_id, created,modified, revision_number, published_state, state)
  values (@n4_tenant, "Test Proc Code", "Test Elec Identifier", "This is a Draft!...Warnings", true, @first_user_on_n4, "draft Equipment Description", @first_chain_sling_id,now(),now(),"REV-1","DRAFT", "ACTIVE");

set @test_proc_def_id = (select max(id) from procedure_definitions);

insert into isolation_points (tenant_id, identifier, source, location, method, check_str, created,createdby, modified,modifiedby)
  values (@n4_tenant, "1", "E", "Isolation point on South side of AHU", "Move E-1 disconnet to off. Apply lock and tag.", "Verify machine is deenergized", now(), @first_user_on_n4,now(), @first_user_on_n4);
set @isolation_point_1 = (select max(id) from isolation_points);

insert into isolation_points (tenant_id, identifier, source, location, method, check_str, created,createdby, modified,modifiedby)
  values (@n4_tenant, "1", "P", "Isolation point on South side of AHU", "Turn W-1 valve to closed position. Apply lock and tag.", "Verify zero gauge pressure", now(), @first_user_on_n4,now(), @first_user_on_n4);
set @isolation_point_2 = (select max(id) from isolation_points);

insert into procedure_definitions_isolation_points values(@test_proc_def_id, @isolation_point_1, 0);
insert into procedure_definitions_isolation_points values(@test_proc_def_id, @isolation_point_2, 1);


#change revision number to LONG.
alter table procedure_definitions drop column revision_number;
alter table procedure_definitions add column revision_number bigint default 0;
update procedure_definitions set revision_number=id;
