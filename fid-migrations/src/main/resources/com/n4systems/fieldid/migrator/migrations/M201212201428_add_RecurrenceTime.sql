drop table if exists temp_recurring;

create table temp_recurring (
  id bigint NOT NULL AUTO_INCREMENT,
  recurrence_type varchar(50) NOT NULL,
  day datetime NULL,
  hour bigint NOT NULL,
  minute bigint NOT NULL,
  PRIMARY KEY (id)
)  ENGINE=InnoDB;
insert into temp_recurring select id, recurrence_type, day, hour, minute from recurring_asset_type_events;

create table recurrence (
  id bigint NOT NULL AUTO_INCREMENT,
  temp_id bigint not null,
  recurrence_type varchar(50) NOT NULL DEFAULT 'WEEKDAYS',
  PRIMARY KEY (id)
) ENGINE=InnoDB;

create table recurrence_time (
  id bigint NOT NULL AUTO_INCREMENT,
  month bigint NULL,
  day bigint NULL,
  hour bigint NOT NULL,
  minute bigint NOT NULL,
  recurrence_id bigint NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_recurrence_time_recurrence FOREIGN KEY (recurrence_id) REFERENCES recurrence(id) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;


insert recurrence (temp_id, recurrence_type)
          select id,recurrence_type from recurring_asset_type_events;

alter table recurring_asset_type_events add column recurrence_id bigint null;

update recurring_asset_type_events rate, recurrence r set rate.recurrence_id = r.id where r.temp_id = rate.id;

insert recurrence_time (recurrence_id, month, day, hour, minute)
          select r.id, MONTH(makedate(2012,day)), DAYOFMONTH(makedate(2012,day)), hour, minute  from recurrence r join recurring_asset_type_events rate on r.temp_id = rate.id;

alter table recurrence drop column temp_id;

alter table recurring_asset_type_events  drop column recurrence_type;
alter table recurring_asset_type_events  drop column hour;
alter table recurring_asset_type_events  drop column minute;
alter table recurring_asset_type_events  drop column day;

alter table recurring_asset_type_events add constraint fk_recurring_asset_type_event_recurrence FOREIGN KEY (recurrence_id) references recurrence(id);
