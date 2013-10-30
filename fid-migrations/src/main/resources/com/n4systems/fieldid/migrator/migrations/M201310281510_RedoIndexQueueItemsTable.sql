drop trigger if exists trig_after_insert_assets;
drop trigger if exists trig_after_update_assets;
drop trigger if exists trig_after_update_users;
drop trigger if exists trig_after_update_org_base;
drop trigger if exists trig_after_update_orders;
drop trigger if exists trig_after_update_predefinedlocations;
drop trigger if exists trig_after_update_assettypes;
drop trigger if exists trig_after_update_assettypegroups;
drop trigger if exists trig_after_update_assetstatus;



CREATE TABLE new_index_queue_items (
  id bigint NOT NULL AUTO_INCREMENT,
  type varchar(32) not null,
  item_id bigint not null,
  tenant_id bigint not null,
  PRIMARY KEY (id),
  CONSTRAINT fk_index_queue_items_on_tenants FOREIGN KEY (tenant_id) REFERENCES tenants (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);


insert into new_index_queue_items (id, type, item_id, tenant_id) (select null, type, id, tenant_id from index_queue_items);

drop table index_queue_items;
rename table new_index_queue_items to index_queue_items;

alter table index_queue_items add unique index (type, item_id);

CREATE TRIGGER trig_after_insert_assets AFTER INSERT ON assets FOR EACH ROW INSERT IGNORE INTO index_queue_items (item_id, type, tenant_id) VALUES (NEW.id, 'ASSET_INSERT', NEW.tenant_id);
CREATE TRIGGER trig_after_update_assets AFTER UPDATE ON assets FOR EACH ROW INSERT IGNORE INTO index_queue_items (item_id, type, tenant_id) VALUES (NEW.id, 'ASSET_UPDATE', NEW.tenant_id);
CREATE TRIGGER trig_after_update_users AFTER UPDATE ON users FOR EACH ROW INSERT IGNORE INTO index_queue_items (item_id, type, tenant_id) VALUES (NEW.id, 'USER', NEW.tenant_id);
CREATE TRIGGER trig_after_update_org_base AFTER UPDATE ON org_base FOR EACH ROW INSERT IGNORE INTO index_queue_items (item_id, type, tenant_id) VALUES (NEW.id, 'ORG', NEW.tenant_id);
CREATE TRIGGER trig_after_update_orders AFTER UPDATE ON orders FOR EACH ROW INSERT IGNORE INTO index_queue_items (item_id, type, tenant_id) VALUES (NEW.id, 'ORDER', NEW.tenant_id);
CREATE TRIGGER trig_after_update_predefinedlocations AFTER UPDATE ON predefinedlocations FOR EACH ROW INSERT IGNORE INTO index_queue_items (item_id, type, tenant_id) VALUES (NEW.id, 'PREDEFINEDLOCATION', NEW.tenant_id);
CREATE TRIGGER trig_after_update_assettypes AFTER UPDATE ON assettypes FOR EACH ROW INSERT IGNORE INTO index_queue_items (item_id, type, tenant_id) VALUES (NEW.id, 'ASSETTYPE', NEW.tenant_id);
CREATE TRIGGER trig_after_update_assettypegroups AFTER UPDATE ON assettypegroups FOR EACH ROW INSERT IGNORE INTO index_queue_items (item_id, type, tenant_id) VALUES (NEW.id, 'ASSETTYPEGROUP', NEW.tenant_id);
CREATE TRIGGER trig_after_update_assetstatus AFTER UPDATE ON assetstatus FOR EACH ROW INSERT IGNORE INTO index_queue_items (item_id, type, tenant_id) VALUES (NEW.id, 'ASSETSTATUS', NEW.tenant_id);