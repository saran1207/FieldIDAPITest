ALTER TABLE dashboard_layouts ADD COLUMN name varchar(50) NOT NULL;

UPDATE dashboard_layouts SET name = (SELECT TRIM(CONCAT(firstname, " ", lastname)) FROM users WHERE id = user_id);

ALTER TABLE dashboard_layouts ADD COLUMN selected tinyint(1) NOT NULL;

UPDATE dashboard_layouts set selected = true;