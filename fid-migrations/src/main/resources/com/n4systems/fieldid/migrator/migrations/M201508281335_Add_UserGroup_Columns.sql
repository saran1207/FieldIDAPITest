alter table assignment_escalation_rules add escalate_to_user_group_id bigint(21);
alter table assignment_escalation_rules add reassign_user_group_id bigint(21);
alter table assignment_escalation_rules modify escalate_to_user_id bigint(21) NULL;
alter table assignment_escalation_rules modify reassign_user_id bigint(21) NULL;