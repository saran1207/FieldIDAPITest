alter table masterevents change event_state workflow_state varchar(255);
alter table saved_reports change eventState workflow_state varchar(255);

update column_mappings set path_expression = "workflowState" where path_expression = "eventState";