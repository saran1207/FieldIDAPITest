ALTER TABLE eventforms ADD use_observation_count_for_result TINYINT(1) NOT NULL;

UPDATE eventforms set use_observation_count_for_result = FALSE;