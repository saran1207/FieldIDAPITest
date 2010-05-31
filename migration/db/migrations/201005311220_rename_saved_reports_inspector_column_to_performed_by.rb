require "saved_report_criteria"
class RenameSavedReportsInspectorColumnToPerformedBy < ActiveRecord::Migration
  def self.up
    SavedReportCriteria.update_all({ :mapkey => "performedBy" },  { :mapkey => "inspector" })
  end
  
  
  def self.down
    SavedReportCriteria.update_all({ :mapkey => "inspector" }, { :mapkey => "performedBy" })
  end
end