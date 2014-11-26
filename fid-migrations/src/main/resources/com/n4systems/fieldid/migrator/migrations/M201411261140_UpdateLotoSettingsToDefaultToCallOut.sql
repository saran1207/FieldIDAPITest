insert into loto_settings (tenant_id, created, modified, annotation_type)
select distinct tenant_id, now(), now(), 'CALL_OUT_STYLE' from procedure_definitions
where tenant_id not in
	(select tenant_id
	from loto_settings);