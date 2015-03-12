CREATE TABLE lockout_reasons (
  id BIGINT NOT NULL AUTO_INCREMENT,
  created DATETIME NOT NULL,
  modified DATETIME NOT NULL,
  modifiedby BIGINT DEFAULT NULL,
  createdby BIGINT DEFAULT NULL,
  tenant_id BIGINT NOT NULL,
  name VARCHAR(255) NOT NULL,
  state VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  KEY fk_lockout_reasons_tenant (`tenant_id`),
  CONSTRAINT fk_lockout_reasons_tenant FOREIGN KEY (tenant_id) REFERENCES tenants (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_lockout_reasons_modifiedby FOREIGN KEY (modifiedby) REFERENCES users (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_lockout_reasons_createdby FOREIGN KEY (createdby) REFERENCES users (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

ALTER TABLE procedures ADD COLUMN lockout_reason_id BIGINT DEFAULT NULL;

ALTER TABLE procedures ADD CONSTRAINT fk_procedures_lockout_reason FOREIGN KEY (lockout_reason_id) REFERENCES  lockout_reasons (id);
