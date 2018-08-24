update column_mappings set sortable = 1 where label = 'label.networklasteventdate';
alter table column_mappings add column ignoreSortAlias TINYINT(1);
update column_mappings set sort_expression = 'networkLastEventDate', ignoreSortAlias = 1 where label = 'label.networklasteventdate';
--
-- rollout script
-- update column_mappings set sortable = 0 where label = 'label.networklasteventdate';
-- alter table column_mappings drop column ignoreSortAlias;
-- update column_mappings set sort_expression = null where label = 'label.networklasteventdate';

