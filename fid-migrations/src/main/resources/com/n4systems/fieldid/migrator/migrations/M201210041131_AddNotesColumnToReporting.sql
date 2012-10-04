INSERT INTO column_mappings(created, modified, label, name, path_expression, sortable, default_order, group_id)
VALUES(now(), now(), 'label.action_notes', 'event_search_action_notes', 'notes', TRUE, 10170, 1)

INSERT into system_column_mappings(column_id) values((SELECT id FROM column_mappings WHERE name ='event_search_action_notes'));