UPDATE masterevents SET status = 'VOID' WHERE status IS NULL;

ALTER TABLE masterevents MODIFY status VARCHAR(255) NOT NULL;