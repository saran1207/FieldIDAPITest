INSERT INTO column_mappings(created, modified, label, name, path_expression, sortable, output_handler, default_order, group_id)
VALUES(now(), now(), 'label.score_percentage', 'event_search_score_percentage', '', FALSE,'com.n4systems.fieldid.viewhelpers.handlers.EventScorePercentageHandler',  1180, 1);

INSERT into system_column_mappings(column_id) values((SELECT id FROM column_mappings WHERE name ='event_search_score_percentage'));