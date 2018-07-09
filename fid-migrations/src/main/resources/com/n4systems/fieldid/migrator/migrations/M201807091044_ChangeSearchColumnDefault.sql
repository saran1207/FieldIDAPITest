delete from active_column_mappings where id = (select acm.id from (select acm.id from active_column_mappings acm
  inner join column_mappings cm on cm.id = acm.mapping_id
  inner join column_layouts cl on cl.id = acm.column_layout_id
where cm.label = 'label.lasteventdate' and name = 'asset_search_lasteventdate' and cl.tenant_id is null and cl.report_type = 'ASSET') as acm);

insert into active_column_mappings (created, modified, mapping_id, ordervalue, column_layout_id)
  select
    NOW(),
    NOW(),
    (select cm.id from column_mappings cm where cm.label = 'label.networklasteventdate'),
    7,
    (select cl.id from column_layouts cl where cl.tenant_id is null and cl.report_type = 'ASSET');