CREATE TABLE sso_global_settings (
  id int NOT NULL,
  max_authentication_age BIGINT NOT NULL,
  CONSTRAINT sso_global_settings_singleton CHECK (ID=1)
);
insert into sso_global_settings (id, max_authentication_age) VALUES(1, 86400);