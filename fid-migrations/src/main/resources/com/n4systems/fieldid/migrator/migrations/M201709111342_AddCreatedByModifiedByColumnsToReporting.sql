INSERT INTO column_mappings(created, modified, label, name, path_expression, sortable, output_handler, default_order, group_id)
VALUES(now(), now(), 'label.createdby', 'event_search_createdby', 'createdBy.displayName', FALSE, NULL,  11020, 1);

INSERT INTO system_column_mappings(column_id) VALUES((SELECT id FROM column_mappings WHERE name ='event_search_createdby'));

INSERT INTO column_mappings(created, modified, label, name, path_expression, sortable, output_handler, default_order, group_id)
VALUES(now(), now(), 'label.modifiedby', 'event_search_modifiedby', 'modifiedBy.displayName', FALSE, NULL,  11030, 1);

INSERT INTO system_column_mappings(column_id) VALUES((SELECT id FROM column_mappings WHERE name ='event_search_modifiedby'));