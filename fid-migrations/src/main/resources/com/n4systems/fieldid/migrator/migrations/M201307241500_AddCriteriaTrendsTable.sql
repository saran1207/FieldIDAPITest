create table criteria_trends
(id bigint not null primary key auto_increment,
created datetime,
modified datetime,
createdby bigint,
modifiedby bigint,
tenant_id bigint not null,
event_form_id bigint,
event_id bigint not null,
completedDate datetime,
dueDate datetime,
event_type_id bigint not null,
owner_id bigint not null,
performedby_id bigint,
assignee_id bigint,
assigned_group_id bigint,
criteria_id bigint,
criteria_section_name varchar(2000),
criteria_name varchar(1000),
result_text varchar(255) );

create index index_criteria_trends_result_text on criteria_trends (result_text);
create index index_criteria_trends_event_type on criteria_trends (event_type_id);
create index index_criteria_trends_tenant on criteria_trends (tenant_id);
create index index_criteria_trends_completed_date on criteria_trends (completedDate);
create index index_criteria_trends_criteria_name on criteria_trends (criteria_name);
create index index_criteria_trends_criteria_section_name on criteria_trends (criteria_section_name);