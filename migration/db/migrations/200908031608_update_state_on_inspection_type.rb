require "inspection_type"
class UpdateStateOnInspectionType < ActiveRecord::Migration
  def self.up
    InspectionType.update_all(:state => "ACTIVE")
    change_column(:inspectiontypes, :state, :string, :null => false)
  end
  
  def self.down
  end
end