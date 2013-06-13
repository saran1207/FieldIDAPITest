CREATE TABLE index_queue_items (
  id BIGINT,
  type VARCHAR(32),
  PRIMARY KEY (id, type)
);

CREATE TRIGGER trig_after_insert_assets AFTER INSERT ON assets FOR EACH ROW INSERT IGNORE INTO index_queue_items (id, type) VALUES (NEW.id, 'ASSET_INSERT');
CREATE TRIGGER trig_after_update_assets AFTER UPDATE ON assets FOR EACH ROW INSERT IGNORE INTO index_queue_items (id, type) VALUES (NEW.id, 'ASSET_UPDATE');
CREATE TRIGGER trig_after_update_users AFTER UPDATE ON users FOR EACH ROW INSERT IGNORE INTO index_queue_items (id, type) VALUES (NEW.id, 'USER');
CREATE TRIGGER trig_after_update_org_base AFTER UPDATE ON org_base FOR EACH ROW INSERT IGNORE INTO index_queue_items (id, type) VALUES (NEW.id, 'ORG');
CREATE TRIGGER trig_after_update_orders AFTER UPDATE ON orders FOR EACH ROW INSERT IGNORE INTO index_queue_items (id, type) VALUES (NEW.id, 'ORDER');
CREATE TRIGGER trig_after_update_predefinedlocations AFTER UPDATE ON predefinedlocations FOR EACH ROW INSERT IGNORE INTO index_queue_items (id, type) VALUES (NEW.id, 'PREDEFINEDLOCATION');
CREATE TRIGGER trig_after_update_assettypes AFTER UPDATE ON assettypes FOR EACH ROW INSERT IGNORE INTO index_queue_items (id, type) VALUES (NEW.id, 'ASSETTYPE');
CREATE TRIGGER trig_after_update_assettypegroups AFTER UPDATE ON assettypegroups FOR EACH ROW INSERT IGNORE INTO index_queue_items (id, type) VALUES (NEW.id, 'ASSETTYPEGROUP');
CREATE TRIGGER trig_after_update_assetstatus AFTER UPDATE ON assetstatus FOR EACH ROW INSERT IGNORE INTO index_queue_items (id, type) VALUES (NEW.id, 'ASSETSTATUS');