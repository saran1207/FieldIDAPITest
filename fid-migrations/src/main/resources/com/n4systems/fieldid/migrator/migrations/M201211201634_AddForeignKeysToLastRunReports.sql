update users set lastRunReportId = null where lastRunReportId not in (select id from saved_reports);
update users set lastRunSearchId = null where lastRunSearchId not in (select id from saved_searches);

alter table users add constraint fk_last_report_on_saved_reports foreign key (lastRunReportId) references saved_reports(id);
alter table users add constraint fk_last_report_on_saved_searches foreign key (lastRunSearchId) references saved_searches(id);