require "saved_report_criteria"
class RenameSavedReportsInspectorColumnToPerformedBy < ActiveRecord::Migration
  def self.up
    SavedReportCriteria.transaction do
      SavedReportCriteria.find(:all, :conditions => { :mapkey => "inspector" }).each do | criteria |
        criteria.mapkey = "performedBy"
        criteria.save
      end
      
    end
  end
end