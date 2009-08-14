require "inspection_type"
class AddEntityStateToInspectionType < ActiveRecord::Migration
  def self.up
    add_column(:inspectiontypes, :state, :string)
    InspectionType.reset_column_information
  end
  
  def self.down
    remove_column(:inspectiontypes, :state)
    InspectionType.reset_column_information
  end
end