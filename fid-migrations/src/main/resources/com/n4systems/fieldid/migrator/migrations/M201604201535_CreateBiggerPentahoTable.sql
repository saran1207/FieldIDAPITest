
/*  ------------------------------------------------------------  */

CREATE TABLE pentaho_events_criteria (
  id bigint(20) AUTO_INCREMENT NOT NULL,
  event_id bigint(20) NOT NULL,
  performedby varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  eventtype varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  assettype varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  location varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  assetstatus varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  eventstatus varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  event_result varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  completedDate datetime DEFAULT NULL,
  identifier varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  tenant bigint(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  criteria_id bigint(20) NOT NULL,
  result_id bigint(20) NOT NULL,
  section varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  criteria varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  selection varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

insert into pentaho_events_criteria (event_id, performedby, eventtype, assettype, location, assetstatus, eventstatus, event_result, completedDate, identifier, tenant, criteria_id, result_id, section, criteria, selection)
select 	pe.event_id,
		pe.performedby,
		pe.eventtype,
		pe.assettype,
		pe.location,
		pe.assetstatus,
		pe.eventstatus,
		pe.event_result,
		pe.completedDate,
		pe.identifier,
		pe.tenant,
		pc.criteria_id,
		pc.result_id,
		pc.section,
		pc.criteria,
		pc.selection
from pentaho_events pe, pentaho_criteria pc
where pc.event_id=pe.event_id;