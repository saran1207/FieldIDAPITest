ALTER TABLE masterevents ADD COLUMN send_email_on_update TINYINT (1);

UPDATE masterevents SET send_email_on_update = true;

UPDATE masterevents SET workflow_state = 'COMPLETED' WHERE workflow_state = 'IN_PROGRESS';