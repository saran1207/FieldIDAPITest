
create table test (
  id bigint,
  name varchar(100),
  lid bigint
);

create table translations (
  id bigint,
  language varchar(10),
  value varchar(1000),
  primary key (id,language)
);

insert into test values(1,'hello',1);
insert into test values(2,'goodbye',2);
insert into test values(3,'what',3);
insert into test values(4,'here',4);


insert into translations values(1, 'fr','bonjour');
insert into translations values(1, 'de','guten tag');
insert into translations values(1, 'ja','hi<japanese>');
insert into translations values(2, 'it','areev adare chee');
insert into translations values(2, 'fr','au revoir');
insert into translations values(3, 'fr','quelle');
insert into translations values(3, 'de','was');
insert into translations values(4, 'fr','ici');

