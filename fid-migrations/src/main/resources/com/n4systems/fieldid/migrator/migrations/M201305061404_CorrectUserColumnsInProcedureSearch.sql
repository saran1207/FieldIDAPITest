update column_mappings set join_expression="unlockedBy,unlockedBy",sort_expression="unlockedBy.firstName,unlockedBy.lastName" where name =  "procedure_search_unlockedby";
update column_mappings set join_expression="lockedBy,lockedBy" where name =  "procedure_search_lockedby";

update column_mappings set path_expression="assigneeName",join_expression="assignedGroup,assignee,assignee", sort_expression="assignedGroup.name,assignee.firstName,assignee.lastName",output_handler=null where name =  "procedure_search_assignee";
