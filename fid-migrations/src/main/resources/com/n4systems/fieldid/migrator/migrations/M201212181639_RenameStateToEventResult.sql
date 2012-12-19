alter table masterevents change status event_result varchar(255);
alter table saved_reports change status event_result varchar(255);
alter table states change status event_result varchar(255);

update column_mappings set path_expression = 'eventResult.displayName', sort_expression='eventResult' where name= 'event_search_eventresult';
update column_mappings set path_expression = 'eventResult' where name= 'event_schedule_status';