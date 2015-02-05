ALTER TABLE eventtypes CHANGE  display_section_totals display_score_section_totals TINYINT(1) NOT NULL;

ALTER TABLE eventtypes ADD (display_observation_section_totals TINYINT(1) NOT NULL, display_observation_percentage TINYINT(1) NOT NULL);

UPDATE eventtypes SET display_observation_section_totals = FALSE, display_observation_percentage = FALSE;

