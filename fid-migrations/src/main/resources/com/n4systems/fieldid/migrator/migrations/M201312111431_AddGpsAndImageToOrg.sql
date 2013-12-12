alter table addressinfo add column latitude decimal(15,10) DEFAULT NULL;
alter table addressinfo add column longitude decimal(15,10) DEFAULT NULL;
alter table org_base add column image_id bigint(20) default null;
alter table org_base add column contactname varchar(150) default null;
alter table org_base add column contactemail varchar(150) default null;
