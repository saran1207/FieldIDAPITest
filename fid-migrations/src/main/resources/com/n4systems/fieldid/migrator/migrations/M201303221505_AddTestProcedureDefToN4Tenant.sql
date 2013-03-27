set @n4_tenant = (select id from tenants where name="n4");
set @chain_sling_asset = (select id from assettypes where tenant_id = @n4_tenant and name = "Chain Sling" );
set @first_chain_sling_id = (select id from assets where type_id = @chain_sling_asset and state="ACTIVE" limit 1);
set @first_user_on_n4 = (select id from users where tenant_id = @n4_tenant and state="ACTIVE" and usertype="FULL" limit 1);

insert into procedure_definitions (tenant_id, procedure_code, electronic_identifier, warnings, complete_points_in_order, developed_by_id, equipment_description, asset_id, created,modified, revision_number) values (@n4_tenant, "Test Proc Code", "Test Elec Identifier", "Test Warnings", true, @first_user_on_n4, "Equipment Description", @first_chain_sling_id,now(),now(),"REV-1");

set @test_proc_def_id = (select max(id) from procedure_definitions);

insert into isolation_points (identifier, source, location, method, check_str) values ("1", "Electrical 480 V", "Isolation point on South side of AHU", "Move E-1 disconnet to off. Apply lock and tag.", "Verify machine is deenergized");
set @isolation_point_1 = (select max(id) from isolation_points);

insert into isolation_points (identifier, source, location, method, check_str) values ("1", "Chilled Water Inlet 60 PSI", "Isolation point on South side of AHU", "Turn W-1 valve to closed position. Apply lock and tag.", "Verify zero gauge pressure");
set @isolation_point_2 = (select max(id) from isolation_points);

insert into procedure_definitions_isolation_points(@test_proc_def_id, @isolation_point_1, 0);
insert into procedure_definitions_isolation_points(@test_proc_def_id, @isolation_point_2, 1);