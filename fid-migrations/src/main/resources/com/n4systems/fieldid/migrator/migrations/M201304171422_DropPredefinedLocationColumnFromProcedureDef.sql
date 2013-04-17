ALTER TABLE procedure_definitions DROP FOREIGN KEY fk_procedure_defs_locations;
ALTER TABLE procedure_definitions DROP COLUMN predefinedlocation_id;
ALTER TABLE procedure_definitions CHANGE location equipment_location varchar(255);
ALTER TABLE procedure_definitions ADD COLUMN building varchar(255) AFTER equipment_location;