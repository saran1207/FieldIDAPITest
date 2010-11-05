class ChangeTablesFromInspectionToEvent < ActiveRecord::Migration

  def self.up
    rename_table(:inspections, :events)
    rename_table(:inspectionsmaster, :masterevents)
    rename_table(:inspectionssub, :subevents)
    rename_table(:inspectionbooks, :eventbooks)
    rename_table(:inspectiongroups, :eventgroups)
    rename_table(:inspectionschedules, :eventschedules)
    rename_table(:inspectiontypes, :eventtypes)
    rename_table(:inspectiontypegroups, :eventtypegroups)
    rename_table(:inspectionsmaster_inspectionssub, :masterevents_subevents)
    rename_table(:associatedinspectiontypes, :associatedeventtypes)

    rename_table(:inspections_fileattachments, :events_fileattachments)
    rename_table(:inspections_infooptionmap, :events_infooptionmap)
    rename_table(:inspectiontypes_criteriasections, :eventtypes_criteriasections)
    rename_table(:inspectiontypes_infofieldnames, :eventtypes_infofieldnames)
    rename_table(:inspectiontypes_supportedprooftests, :eventtypes_supportedprooftests)

    rename_table(:notificationsettings_inspectiontypes, :notificationsettings_eventtypes)
    rename_table(:catalogs_inspectiontypes, :catalogs_eventtypes)
  end

  def self.down
    rename_table(:events, :inspections)
    rename_table(:masterevents, :inspectionsmaster)
    rename_table(:subevents, :inspectionssub)
    rename_table(:eventbooks, :inspectionbooks)
    rename_table(:eventgroups, :inspectiongroups)
    rename_table(:eventschedules, :inspectionschedules)
    rename_table(:eventtypes, :inspectiontypes)
    rename_table(:eventtypegroups, :inspectiontypegroups)
    rename_table(:masterevents_subevents, :inspectionsmaster_inspectionssub)
    rename_table(:associatedeventtypes, :associatedinspectiontypes)

    rename_table(:events_fileattachments, :inspections_fileattachments)
    rename_table(:events_infooptionmap, :inspections_infooptionmap)
    rename_table(:eventtypes_criteriasections, :inspectiontypes_criteriasections)
    rename_table(:eventtypes_infofieldnames, :inspectiontypes_infofieldnames)
    rename_table(:eventtypes_supportedprooftests, :inspectiontypes_supportedprooftests)

    rename_table(:notificationsettings_eventtypes, :notificationsettings_inspectiontypes)
    rename_table(:catalogs_eventtypes, :catalogs_inspectiontypes)
  end

end