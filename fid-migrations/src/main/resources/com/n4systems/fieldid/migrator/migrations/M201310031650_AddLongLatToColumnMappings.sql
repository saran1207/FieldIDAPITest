UPDATE column_mappings SET default_order = 10035 WHERE name='asset_search_identified';
UPDATE column_mappings SET default_order = 10171 WHERE name='event_search_action_notes';

INSERT INTO column_mappings(created, modified, label, name, path_expression, sortable, default_order, group_id)
                     VALUES(now(), now(), 'label.latitude', 'event_search_latitude', 'gpsLocation.latitude', FALSE, 10172, 1);
INSERT INTO column_mappings(created, modified, label, name, path_expression, sortable, default_order, group_id)
                     VALUES(now(), now(), 'label.longitude', 'event_search_longitude', 'gpsLocation.longitude', FALSE, 10173, 1);

INSERT INTO column_mappings(created, modified, label, name, path_expression, sortable, default_order, group_id)
  VALUES(now(), now(), 'label.latitude', 'asset_search_latitude', 'gpsLocation.latitude', FALSE, 10230, 17);
INSERT INTO column_mappings(created, modified, label, name, path_expression, sortable, default_order, group_id)
  VALUES(now(), now(), 'label.longitude', 'asset_search_longitude', 'gpsLocation.longitude', FALSE, 10231, 17);


INSERT into system_column_mappings(column_id) values((SELECT id FROM column_mappings WHERE name ='event_search_latitude'));
INSERT into system_column_mappings(column_id) values((SELECT id FROM column_mappings WHERE name ='event_search_longitude'));
INSERT into system_column_mappings(column_id) values((SELECT id FROM column_mappings WHERE name ='asset_search_latitude'));
INSERT into system_column_mappings(column_id) values((SELECT id FROM column_mappings WHERE name ='asset_search_longitude'));

