alter table procedure_definitions add column auth_notification_sent tinyint(1) not null;
update procedure_definitions set auth_notification_sent = 0;