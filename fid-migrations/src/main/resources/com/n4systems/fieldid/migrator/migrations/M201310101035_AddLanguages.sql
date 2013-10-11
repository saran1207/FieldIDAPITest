CREATE TABLE languages (
  id bigint NOT NULL AUTO_INCREMENT,
  locale VARCHAR(10),
  PRIMARY KEY (id)
);

INSERT INTO languages (language) VALUES
('da'),
('fr'),
('de'),
('it'),
('lv'),
('es'),
('sv');

