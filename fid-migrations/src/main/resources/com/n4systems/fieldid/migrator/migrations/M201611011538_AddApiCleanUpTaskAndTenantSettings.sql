alter table tenant_settings add api_limit bigint(20) default 10000;

insert into tasks (id, created, modified, classname, cronexpression, taskgroup, enabled) values ('PublicApiLogCleanupTask', now(), now(), 'com.n4systems.taskscheduling.task.PublicApiCleanupTask', '* * 1 * *', 'default', 0);