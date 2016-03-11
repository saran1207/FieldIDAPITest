CREATE TABLE pentaho_test (
  id bigint(20) AUTO_INCREMENT NOT NULL,
  performedby varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  eventtype varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  assettype varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  location varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  assetstatus varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  eventstatus varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  event_result varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  completedDate datetime DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;



insert into pentaho_test (performedby, eventtype, assettype, location, assetstatus, eventstatus, event_result, completedDate)
select u.firstname, et.name, at.name, m.location, astat.name, estat.name, m.event_result, m.completedDate
from eventstatus estat, assetstatus astat, assettypes at, eventtypes et, events e, masterevents m, assets a, users u
where e.asset_id=a.id and astat.id=e.assetstatus_id and at.id=a.type_id and et.id=e.type_id and e.id=m.id and u.id=m.performedby_id and estat.id=e.eventstatus_id and e.tenant_id=15511493;