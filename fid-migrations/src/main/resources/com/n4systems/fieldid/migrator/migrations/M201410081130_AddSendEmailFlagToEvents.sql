ALTER TABLE masterevents ADD COLUMN send_email_on_update TINYINT (1);

UPDATE masterevents SET send_email_on_update = true;