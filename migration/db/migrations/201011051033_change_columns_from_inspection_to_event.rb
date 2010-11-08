class ChangeColumnsFromInspectionToEvent < ActiveRecord::Migration

  def self.up
    change_column(:assettypeschedules, :inspectiontype_id, :index_producttypeschedules_on_inspectiontype_id, :fk_producttypeschedules_inspectiontypes, :eventtype_id, :index_assettypeschedules_on_eventtype_id, :fk_assettypeschedules_inspectiontypes, :eventtype_id, :eventtypes, :id, "BIGINT(20) DEFAULT NULL")
    change_column(:associatedeventtypes, :inspectiontype_id, :fk_associatedinspectiontypes_inspectiontypes, :fk_associatedinspectiontypes_inspectiontypes, :eventtype_id, :index_associatedinspectiontypes_on_eventtype_id, :fk_associatedeventtypes_eventtypes, :eventtype_id, :eventtypes, :id, "BIGINT(20) DEFAULT NULL")
    change_column(:criteriaresults, :inspection_id, :index_criteriaresults_on_inspection_id, :fk_criteriaresults_inspections, :event_id, :index_criteriaresults_on_event_id, :fk_criteriaresults_events, :event_id, :events, :id, "BIGINT(20) NOT NULL")
    change_column(:masterevents_subevents, :inspectionsmaster_inspection_id, :fk_masterinspections_inspection, :fk_masterinspections_inspection, :masterevents_event_id, :index_masterevents_subevents_on_event_id, :fk_masterevents_inspection, :masterevents_event_id, :events, :id, "BIGINT(20) NOT NULL")
    change_column(:masterevents_subevents, :subinspections_inspection_id, :index_inspectionsmaster_inspectionssub_on_subinspections_inspec, :fk_subinspections_inspection, :subevents_event_id, :index_masterevents_subevents_on_subevents_event, :fk_subinspections_inspection, :subevents_event_id, :events, :id, "BIGINT(20) NOT NULL", true)
    change_column(:eventschedules, :inspectiontype_id, :index_inspectionschedules_on_inspectiontype_id, :fk_inspectionschedules_inspectiontypes, :eventtype_id, :index_eventschedules_on_eventtype_id, :fk_eventschedules_eventtypes, :eventtype_id, :eventtypes, :id, "BIGINT(20) DEFAULT NULL")
    change_column(:eventschedules, :inspection_inspection_id, :index_inspectionschedules_on_inspection_inspection_id, nil, :event_event_id, :index_eventschedules_on_event_event_id, :fk_eventschedules_events, :event_event_id, :events, :id, "BIGINT(20) DEFAULT NULL", true)
    change_column(:eventtypes_criteriasections, :inspectiontypes_id, :fk_inspectiontypes_criteriasections_inspectiontypes, :fk_inspectiontypes_criteriasections_inspectiontypes, :eventtypes_id, :fk_eventtypes_criteriasections_eventtypes, :fk_eventtypes_criteriasections_eventtypes, :eventtypes_id, :eventtypes, :id, "BIGINT(20) NOT NULL")
    change_column(:eventtypes_supportedprooftests, :inspectiontypes_id, :index_inspectiontypes_supportedprooftests_on_inspectiontypes_id, :fk_inspectiontypes_supportedprooftests_inspectiontypes, :eventtypes_id, :index_eventtypes_supportedprooftests_on_eventtypes_id, :fk_eventtypes_supportedprooftests_eventtypes, :eventtypes_id, :eventtypes, :id, "BIGINT(20) NOT NULL")
    change_column(:eventtypes_infofieldnames, :inspectiontypes_id, :fk_inspectiontypes_infofieldnames_inspectiontypes, :fk_inspectiontypes_infofieldnames_inspectiontypes, :eventtypes_id, :fk_eventtypes_infofieldnames_eventtypes, :fk_eventtypes_infofieldnames_eventtypes, :eventtypes_id, :eventtypes, :id, "BIGINT(20) NOT NULL")
    change_column(:subevents, :inspection_id, :index_inspectionssub_on_inspection_id, :fk_inspectionssub_inspections, :event_id, :index_subevents_on_event_id, :fk_subevents_events, :event_id, :events, :id, "BIGINT(20) NOT NULL")
    change_column(:catalogs_eventtypes, :publishedinspectiontypes_id, nil, :fk_catalogs_inspectiontypes_inspectiontypes, :publishedeventtypes_id, nil, :fk_catalogs_eventtypes_eventtypes, :publishedeventtypes_id, :eventtypes, :id, "BIGINT(20) NOT NULL")
    change_column(:notificationsettings_eventtypes, :inspectiontype_id, nil, :fk_notificationsettings_inspectiontypes_notificationsettings, :eventtype_id, :index_notificationsettings_eventtypes, :fk_notificationsettings_eventtypes, :eventtype_id, :eventtypes, :id, "BIGINT(20) NOT NULL")
    change_column(:events_fileattachments, :inspections_id, :index_inspections_fileattachments_on_inspections_id, :fk_inspections_fileattachments_inspections, :events_id, :index_events_fileattachments_on_events_id, :fk_events_fileattachments_events, :events_id, :events, :id, "BIGINT(20) NOT NULL")
    change_column(:events_infooptionmap, :inspections_id, :fk_inspections_infooptionmap_inspections, :fk_inspections_infooptionmap_inspections, :events_id, :fk_events_infooptionmap_events, :fk_events_infooptionmap_events, :events_id, :events, :id, "BIGINT(20) NOT NULL")

    execute "alter table setupdatalastmoddates change column inspectiontypes eventtypes datetime not null"
    execute "alter table printouts change column withsubinspections withsubevents tinyint(1) default null"
    execute "alter table assets change column lastinspectiondate lasteventdate datetime default null"
  end

  def self.down
    change_column(:assettypeschedules, :eventtype_id, :index_assettypeschedules_on_eventtype_id, :fk_assettypeschedules_inspectiontypes, :inspectiontype_id, :index_producttypeschedules_on_inspectiontype_id, :fk_producttypeschedules_inspectiontypes, :inspectiontype_id, :eventtypes, :id, "BIGINT(20) DEFAULT NULL")
    change_column(:associatedeventtypes, :eventtype_id, :index_associatedinspectiontypes_on_eventtype_id, :fk_associatedeventtypes_eventtypes, :inspectiontype_id, :fk_associatedinspectiontypes_inspectiontypes, :fk_associatedinspectiontypes_inspectiontypes, :associatedeventtypes, :eventtypes, :id, "BIGINT(20) DEFAULT NULL")
    change_column(:criteriaresults, :event_id, :index_criteriaresults_on_event_id, :fk_criteriaresults_events, :inspection_id, :index_criteriaresults_on_inspection_id, :fk_criteriaresults_inspections, :inspection_id, :events, :id, "BIGINT(20) NOT NULL")
    change_column(:masterevents_subevents, :masterevents_event_id, :index_masterevents_subevents_on_event_id, :fk_masterevents_inspection, :inspectionsmaster_inspection_id, :inspectionsmaster_inspection_id, :fk_masterinspections_inspection, :inspectionsmaster_inspection_id, :events, :id, "BIGINT(20) NOT NULL")
    change_column(:masterevents_subevents, :subevents_event_id, :index_masterevents_subevents_on_subevents_event, :fk_subinspections_inspection, :subinspections_inspection_id, :index_inspectionsmaster_inspectionssub_on_subinspections_inspec, :fk_subinspections_inspection, :subinspections_inspection_id, :events, :id, "BIGINT(20) NOT NULL", true)
    change_column(:eventschedules, :eventtype_id, :index_eventschedules_on_eventtype_id, :fk_eventschedules_eventtypes, :inspectiontype_id, :index_inspectionschedules_on_inspectiontype_id, :fk_inspectionschedules_inspectiontypes, :inspectiontype_id, :eventtypes, :id, "BIGINT(20) DEFAULT NULL")
    change_column(:eventschedules, :event_event_id, :index_eventschedules_on_event_event_id, :fk_eventschedules_events, :inspection_inspection_id, :index_inspectionschedules_on_inspection_inspection_id, :fk_eventschedules_events, :inspection_inspection_id, :events, :id, "BIGINT(20) DEFAULT NULL", true)
    change_column(:eventtypes_criteriasections, :eventtypes_id, :fk_eventtypes_criteriasections_eventtypes, :fk_eventtypes_criteriasections_eventtypes, :inspectiontypes_id, :fk_inspectiontypes_criteriasections_inspectiontypes, :fk_inspectiontypes_criteriasections_inspectiontypes, :inspectiontypes_id, :eventtypes, :id, "BIGINT(20) NOT NULL")
    change_column(:eventtypes_supportedprooftests, :eventtypes_id, :index_eventtypes_supportedprooftests_on_eventtypes_id, :fk_eventtypes_supportedprooftests_eventtypes, :inspectiontypes_id, :index_inspectiontypes_supportedprooftests_on_inspectiontypes_id, :fk_inspectiontypes_supportedprooftests_inspectiontypes, :inspectiontypes_id, :eventtypes, :id, "BIGINT(20) NOT NULL")
    change_column(:eventtypes_infofieldnames, :eventtypes_id, :fk_eventtypes_infofieldnames_eventtypes, :fk_eventtypes_infofieldnames_eventtypes, :inspectiontypes_id, :fk_inspectiontypes_infofieldnames_inspectiontypes, :fk_inspectiontypes_infofieldnames_inspectiontypes, :inspectiontypes_id, :eventtypes, :id, "BIGINT(20) NOT NULL")
    change_column(:subevents, :event_id, :index_subevents_on_event_id, :fk_subevents_events, :inspection_id, :index_inspectionssub_on_inspection_id, :fk_inspectionssub_inspections, :inspection_id, :events, :id, "BIGINT(20) NOT NULL")
    change_column(:catalogs_eventtypes, :publishedeventtypes_id, nil, :fk_catalogs_eventtypes_eventtypes, :publishedinspectiontypes_id, nil, :fk_catalogs_inspectiontypes_inspectiontypes, :publishedeventtypes_id, :eventtypes, :id, "BIGINT(20) NOT NULL")
    change_column(:notificationsettings_eventtypes, :eventtype_id, :index_notificationsettings_eventtypes, :fk_notificationsettings_eventtypes, :inspectiontype_id, nil, :fk_notificationsettings_inspectiontypes_notificationsettings, :inspectiontype_id, :eventtypes, :id, "BIGINT(20) NOT NULL")
    change_column(:events_fileattachments, :events_id, :index_events_fileattachments_on_events_id, :fk_events_fileattachments_events, :inspections_id, :index_inspections_fileattachments_on_inspections_id, :fk_inspections_fileattachments_inspections, :inspections_id, :events, :id, "BIGINT(20) NOT NULL")
    change_column(:events_infooptionmap, :events_id, :fk_events_infooptionmap_events, :fk_events_infooptionmap_events, :events_id, :fk_inspections_infooptionmap_inspections, :fk_inspections_infooptionmap_inspections, :inspections_id, :events, :id, "BIGINT(20) NOT NULL")

    execute "alter table setupdatalastmoddates change column eventtypeseventtypes inspectiontypes datetime not null"
    execute "alter table printouts change column withsubevents withsubinspections tinyint(1) default null"
    execute "alter table assets change column lasteventdate lastinspectiondate datetime default null"
  end

  def self.change_column(table, old_column_name, old_index_name, old_fk_name, new_column_name, new_index_name, new_fk_name, fk_column, fk_target_table, fk_target_column, column_def = "BIGINT(20)", unique=false)
    execute "alter table #{table} drop foreign key #{old_fk_name}" unless old_fk_name.nil?
    execute "alter table #{table} drop key #{old_index_name}" unless old_index_name.nil?
    execute "alter table #{table} change column #{old_column_name} #{new_column_name} #{column_def}"
    add_index table, [new_column_name], :name => new_index_name, :unique => unique unless new_index_name.nil?
    execute "alter table #{table} add foreign key #{new_fk_name} (#{fk_column}) references #{fk_target_table} (#{fk_target_column}) on delete no action on update no action" unless new_fk_name.nil?
  end

end