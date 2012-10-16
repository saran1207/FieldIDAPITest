ALTER TABLE masterevents CHANGE nextDate dueDate datetime;

UPDATE column_mappings SET path_expression='dueDate' WHERE name='event_search_scheduled_date';
