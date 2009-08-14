
class AddArchiveNameToInspectionType < ActiveRecord::Migration
  def self.up
    add_column(:inspectiontypes, :archivedName, :string)      
  end
  
  def self.down
    remove_column(:inspectiontypes, :archivedName)
  end
end