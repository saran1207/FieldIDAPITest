CREATE TABLE image_annotation_type (
  id bigint NOT NULL AUTO_INCREMENT,
  name varchar(255) DEFAULT NULL,
  identifier varchar(255) DEFAULT NULL,
  font_color varchar(255) DEFAULT NULL,
  background_color varchar(255) DEFAULT NULL,
  border_color varchar(255) DEFAULT NULL,
  icon varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

INSERT INTO image_annotation_type (name, identifier, font_color, background_color, border_color, icon)
VALUES
('Control Panel', 'CP', '#D68741', '#FFFFFF', '#D68741', ''),
('Electrical', 'E', '#D52029', '#FFFFFF', '#D52029', ''),
('Gas', 'G', '#7C4075', '#FFFFFF', '#7C4075', ''),
('Pneumatic', 'P', '#FFFFFF', '#144B8F', '#FFFFFF', ''),
('Steam', 'S', '#FFFFFF', '#D83E37', '#FFFFFF', ''),
('Valve', 'V', '#000000', '#FFFFFF', '#000000', ''),
('Water', 'W', '#FFFFFF', '#099C4F', '#FFFFFF', '');