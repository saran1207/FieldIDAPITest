alter table procedure_definitions add column asset_id bigint not null;

alter table procedure_definitions add constraint fk_procedure_definitions_assets foreign key (asset_id) references assets (id);