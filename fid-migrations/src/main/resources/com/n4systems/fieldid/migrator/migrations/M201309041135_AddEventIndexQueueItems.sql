drop table if exists event_index_queue_items//

CREATE TABLE event_index_queue_items (
  id bigint not null auto_increment,
  type varchar(32) not null,
  item_id BIGINT not null,
  PRIMARY KEY (id)
)//

alter table event_index_queue_items add unique index (type, item_id)//

drop trigger if exists trig_after_insert_events//
drop trigger if exists trig_after_update_events//

CREATE TRIGGER trig_after_insert_events AFTER INSERT ON masterevents
FOR EACH ROW
BEGIN
	INSERT IGNORE INTO criteria_trends_index_queue_items (item_id, type) VALUES (NEW.event_id, 'EVENT_INSERT');
	INSERT IGNORE INTO event_index_queue_items (item_id, type) VALUES (NEW.event_id, 'EVENT_INSERT');
END//

CREATE TRIGGER trig_after_update_events AFTER UPDATE ON masterevents
FOR EACH ROW
BEGIN
	INSERT IGNORE INTO criteria_trends_index_queue_items (item_id, type) VALUES (NEW.event_id, 'EVENT_UPDATE');
	INSERT IGNORE INTO event_index_queue_items (item_id, type) VALUES (NEW.event_id, 'EVENT_UPDATE');
END//
