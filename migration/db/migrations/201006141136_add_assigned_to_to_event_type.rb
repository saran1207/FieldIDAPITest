require "inspection_type"
class AddAssignedToToEventType < ActiveRecord::Migration
  def self.up
    add_column(:inspectiontypes, :assignedtoavailable, :boolean)
    InspectionType.reset_column_information
    InspectionType.update_all({ :assignedtoavailable => false })
    change_column(:inspectiontypes, :assignedtoavailable, :boolean, :null => false)
  end
  
  def self.down
    remove_column(:inspectiontypes, :assignedtoavailable)
    InspectionType.reset_column_information
  end
end