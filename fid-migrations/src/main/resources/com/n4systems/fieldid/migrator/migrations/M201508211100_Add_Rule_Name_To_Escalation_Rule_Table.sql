alter table assignment_escalation_rules add rule_name varchar(255) NOT NULL after id;
alter table assignment_escalation_rules drop notify_assignee;
alter table assignment_escalation_rules add notifyAssignee tinyint(1);