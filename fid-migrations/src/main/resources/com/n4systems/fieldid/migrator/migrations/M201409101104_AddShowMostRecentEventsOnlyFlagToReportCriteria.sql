ALTER TABLE saved_reports ADD COLUMN showMostRecentEventsOnly TINYINT(1);

UPDATE saved_reports SET showMostRecentEventsOnly = FALSE;