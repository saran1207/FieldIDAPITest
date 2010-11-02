require "saved_report_criteria"

class ChangeSavedReportCriteriaFromInspectionToEvent < ActiveRecord::Migration

  def self.up
    SavedReportCriteria.update_all({ :mapkey => "eventBook" },  { :mapkey => "inspectionBook" })
    SavedReportCriteria.update_all({ :mapkey => "eventTypeGroup" },  { :mapkey => "inspectionTypeGroup" })
  end

  def self.down
    SavedReportCriteria.update_all({ :mapkey => "inspectionBook" },  { :mapkey => "eventBook" })
    SavedReportCriteria.update_all({ :mapkey => "inspectionTypeGroup" },  { :mapkey => "eventTypeGroup" })
  end

end