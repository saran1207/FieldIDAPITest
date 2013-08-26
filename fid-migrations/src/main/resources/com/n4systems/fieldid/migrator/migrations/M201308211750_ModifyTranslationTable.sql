drop table if exists translations;
create table translations (
  ognl varchar(50),
  entity_id bigint,
  language varchar(10),
  value varchar(1000),
  primary key (ognl,entity_id,language)
);
