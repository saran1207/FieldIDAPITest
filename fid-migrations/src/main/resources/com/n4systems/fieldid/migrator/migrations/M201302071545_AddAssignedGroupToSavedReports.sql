alter table saved_reports add column assignedUserGroup bigint(20), add column unassignedOnly boolean not null default 0;
update saved_reports set unassignedOnly=true where assignedUser = 0;
update saved_reports set assignedUser = null where assignedUser = 0;