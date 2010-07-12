require "inspection_schedule"

class AddAdvancedLocationToInspectionSchedules < ActiveRecord::Migration
  def self.up
    add_column(:inspectionschedules, :predefinedlocation_id, :integer)
    add_foreign_key(:inspectionschedules, :predefinedlocations, :source_column => :predefinedlocation_id)
    
    InspectionSchedule.reset_column_information
    
    InspectionSchedule.update_all({:location => ""}, {:location => nil})
    
    change_column(:inspectionschedules, :location, :string, :null => false)
  end
  
  
  def self.down
    drop_foreign_key(:inspectionschedules, :predefinedlocations, :source_column => :predefinedlocation_id)
    remove_column(:inspectionschedules, :predefinedlocation_id)
    change_column(:inspectionschedules, :location, :string, :null => true)
  end
end