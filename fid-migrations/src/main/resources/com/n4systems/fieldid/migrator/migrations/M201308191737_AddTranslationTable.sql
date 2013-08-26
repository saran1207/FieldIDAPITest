create table translations (
  id bigint,
  language varchar(10),
  value varchar(1000),
  primary key (id,language)
);

