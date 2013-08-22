create table test (
  id bigint,
  text varchar(100)
);

create table translations (
  ognl varchar(50),
  entity_id bigint,
  language varchar(10),
  value varchar(1000),
  primary key (ognl,entity_id,language)
);

insert into test values(1,'hello');
insert into test values(2,'goodbye');
insert into test values(3,'what');
insert into test values(4,'here');

insert into translations values('test.text', 1, 'fr','bonjour');
insert into translations values('test.text', 1, 'de','guten tag');
insert into translations values('test.text', 1, 'ja','hi<japanese>');
insert into translations values('test.text', 2, 'it','areev adare chee');
insert into translations values('test.text', 2, 'fr','au revoir');
insert into translations values('test.text', 3, 'fr','quelle');
insert into translations values('test.text', 3, 'de','was');
insert into translations values('test.text', 4, 'fr','ici');

