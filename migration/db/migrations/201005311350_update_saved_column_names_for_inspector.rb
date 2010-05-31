require "saved_report_column"
class UpdateSavedColumnNamesForInspector < ActiveRecord::Migration
  def self.up
     SavedReportColumn.update_all({:element => "inspection_search_performed_by" }, { :element => "inspection_search_inspector" })
  end
  
  def self.down
    SavedReportColumn.update_all({:element => "inspection_search_inspector" }, { :element => "inspection_search_performed_by" })
  end
end