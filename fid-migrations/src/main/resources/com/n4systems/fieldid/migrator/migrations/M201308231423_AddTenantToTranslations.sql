drop table if exists translations;

create table translations (
  tenant_id bigint not null,
  ognl varchar(50) not null,
  entity_id bigint not null,
  language varchar(10),
  value varchar(1000),
  primary key (tenant_id, entity_id, ognl, language),
  constraint fk_translation_tenant foreign key (tenant_id) references tenants(id)
);



insert into translations values((select id from tenants where name='n4'), 'test.text', 1, 'fr','bonjour');
insert into translations values((select id from tenants where name='n4'),'test.text', 1, 'de','guten tag');
insert into translations values((select id from tenants where name='n4'),'test.text', 1, 'ja','hi<japanese>');
insert into translations values((select id from tenants where name='n4'),'test.text', 2, 'it','areev adare chee');
insert into translations values((select id from tenants where name='n4'),'test.text', 2, 'fr','au revoir');
insert into translations values((select id from tenants where name='n4'),'test.text', 3, 'fr','quelle');
insert into translations values((select id from tenants where name='n4'),'test.text', 3, 'de','was');
insert into translations values((select id from tenants where name='n4'),'test.text', 4, 'fr','ici');




drop table if exists test;
create table test (
  id bigint,
  tenant_id bigint,
  text varchar(100),
  created datetime,
  modified datetime,
  createdby bigint,
  modifiedby bigint,
  constraint fk_test_tenant foreign key (tenant_id) references tenants(id)
);


insert into test(id, text) values(1,'hello');
insert into test(id, text) values(2,'goodbye');
insert into test(id, text) values(3,'what');
insert into test(id, text) values(4,'here');

update test set tenant_id = (select id from tenants where name='n4');
