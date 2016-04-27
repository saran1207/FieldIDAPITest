CREATE TABLE pentaho_events (
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
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


insert into pentaho_events (event_id, performedby, eventtype, assettype, location, assetstatus, eventstatus, event_result, completedDate, identifier, tenant)
select 	e.id,
		u.firstname,
		et.name,
		at.name,
		me.location,
		astat.name,
		es.name,
		me.event_result,
		me.completedDate,
		a.identifier,
		t.id
from events e
	 left join masterevents me on e.id=me.id
	 left join eventstatus es on e.eventstatus_id=es.id
	 left join assetstatus astat on astat.id=e.assetstatus_id
	 left join eventtypes et on et.id=e.type_id
	 left join assets a on a.id=e.asset_id
	 left join assettypes at on at.id=a.type_id
	 left join users u on u.id=me.performedby_id
	 left join tenants t on t.id=e.tenant_id
where
		me.state='ACTIVE'
		and me.workflow_state='COMPLETED';


CREATE TABLE pentaho_criteria (
  id bigint(20) AUTO_INCREMENT NOT NULL,
  event_id bigint(20) NOT NULL,
  criteria_id bigint(20) NOT NULL,
  result_id bigint(20) NOT NULL,
  section varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  criteria varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  selection varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


insert into pentaho_criteria (event_id, criteria_id, result_id, section, criteria, selection)
select 	pe.event_id as event_id,
		c.id as criteria_id,
		cbcr.id as result_id,
		cs.title as secion,
		c.displaytext as criteria,
		cbcr.value as selection
from pentaho_events pe
	join events e on pe.event_id=e.id
	join eventtypes et on e.type_id=et.id
	join eventforms ef on et.eventform_id=ef.id
	join eventforms_criteriasections efc on ef.id=efc.eventform_id
	join criteriasections cs on efc.sections_id=cs.id
	join criteriasections_criteria csc on cs.id=csc.criteriasections_id
	join criteria c on csc.criteria_id=c.id
	join criteriaresults cr on cr.criteria_id=c.id
	join combobox_criteriaresults cbcr on cbcr.id=cr.id;


insert into pentaho_criteria (event_id, criteria_id, result_id, section, criteria, selection)
select 	pe.event_id as event_id,
		c.id as criteria_id,
		cbcr.id as result_id,
		cs.title as secion,
		c.displaytext as criteria,
		cbcr.value as selection
from pentaho_events pe
	join events e on pe.event_id=e.id
	join eventtypes et on e.type_id=et.id
	join eventforms ef on et.eventform_id=ef.id
	join eventforms_criteriasections efc on ef.id=efc.eventform_id
	join criteriasections cs on efc.sections_id=cs.id
	join criteriasections_criteria csc on cs.id=csc.criteriasections_id
	join criteria c on csc.criteria_id=c.id
	join criteriaresults cr on cr.criteria_id=c.id
	join datefield_criteriaresults cbcr on cbcr.id=cr.id;

	
insert into pentaho_criteria (event_id, criteria_id, result_id, section, criteria, selection)
select 	pe.event_id as event_id,
		c.id as criteria_id,
		cbcr.id as result_id,
		cs.title as secion,
		c.displaytext as criteria,
		cbcr.value as selection
from pentaho_events pe
	join events e on pe.event_id=e.id
	join eventtypes et on e.type_id=et.id
	join eventforms ef on et.eventform_id=ef.id
	join eventforms_criteriasections efc on ef.id=efc.eventform_id
	join criteriasections cs on efc.sections_id=cs.id
	join criteriasections_criteria csc on cs.id=csc.criteriasections_id
	join criteria c on csc.criteria_id=c.id
	join criteriaresults cr on cr.criteria_id=c.id
	join numberfield_criteriaresults cbcr on cbcr.id=cr.id;

insert into pentaho_criteria (event_id, criteria_id, result_id, section, criteria, selection)
select 	pe.event_id as event_id,
		c.id as criteria_id,
		cbcr.id as result_id,
		cs.title as secion,
		c.displaytext as criteria,
		cbcr.value as selection
from pentaho_events pe
	join events e on pe.event_id=e.id
	join eventtypes et on e.type_id=et.id
	join eventforms ef on et.eventform_id=ef.id
	join eventforms_criteriasections efc on ef.id=efc.eventform_id
	join criteriasections cs on efc.sections_id=cs.id
	join criteriasections_criteria csc on cs.id=csc.criteriasections_id
	join criteria c on csc.criteria_id=c.id
	join criteriaresults cr on cr.criteria_id=c.id
	join select_criteriaresults cbcr on cbcr.id=cr.id;

insert into pentaho_criteria (event_id, criteria_id, result_id, section, criteria, selection)
select 	pe.event_id as event_id,
		c.id as criteria_id,
		cbcr.id as result_id,
		cs.title as secion,
		c.displaytext as criteria,
		cbcr.value as selection
from pentaho_events pe
	join events e on pe.event_id=e.id
	join eventtypes et on e.type_id=et.id
	join eventforms ef on et.eventform_id=ef.id
	join eventforms_criteriasections efc on ef.id=efc.eventform_id
	join criteriasections cs on efc.sections_id=cs.id
	join criteriasections_criteria csc on cs.id=csc.criteriasections_id
	join criteria c on csc.criteria_id=c.id
	join criteriaresults cr on cr.criteria_id=c.id
	join textfield_criteriaresults cbcr on cbcr.id=cr.id;



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
