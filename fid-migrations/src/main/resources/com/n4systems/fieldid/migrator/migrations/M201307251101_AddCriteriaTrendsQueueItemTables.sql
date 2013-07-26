CREATE TABLE criteria_trends_index_queue_items (
  id bigint not null auto_increment,
  type varchar(32) not null,
  item_id BIGINT not null,
  PRIMARY KEY (id)
);

alter table criteria_trends_index_queue_items add unique index (type, item_id);