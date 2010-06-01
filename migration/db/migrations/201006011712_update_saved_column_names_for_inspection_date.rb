require "saved_report_column"
class UpdateSavedColumnNamesForInspectionDate < ActiveRecord::Migration
  def self.up
     SavedReportColumn.update_all({ :element => "inspection_search_date_performed" }, { :element => "inspection_search_inspectiondate" })
  end
  
  def self.down
    SavedReportColumn.update_all({ :element => "inspection_search_inspectiondate" }, { :element => "inspection_search_date_performed" })
  end
end