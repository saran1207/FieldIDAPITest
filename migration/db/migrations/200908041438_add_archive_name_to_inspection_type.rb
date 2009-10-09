require "inspection_type"
class AddArchiveNameToInspectionType < ActiveRecord::Migration
  def self.up
    add_column(:inspectiontypes, :archivedName, :string)      
    InspectionType.reset_column_information
  end
  
  def self.down
    remove_column(:inspectiontypes, :archivedName)
  end
end