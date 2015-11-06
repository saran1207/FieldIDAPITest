alter table masterevents modify assignee_id BIGINT(20);
alter table masterevents modify project_id BIGINT(20);

alter table events modify eventstatus_id BIGINT(20);

alter table eventstatus modify modifiedby BIGINT(20);
alter table eventstatus modify createdby BIGINT(20);

alter table eventaudit modify modifiedby BIGINT(20);

alter table eventaudit_event modify event_id BIGINT(20);
alter table eventaudit_event modify eventaudit_id BIGINT(20);

alter table eventstatus modify tenant_id BIGINT(20);