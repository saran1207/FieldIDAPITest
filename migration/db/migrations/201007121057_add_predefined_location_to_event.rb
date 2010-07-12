require 'inspectionmaster'
class AddPredefinedLocationToEvent < ActiveRecord::Migration
  def self.up
    add_column(:inspectionsmaster, :predefinedlocation_id, :integer)
    add_foreign_key(:inspectionsmaster, :predefinedlocations, :source_column => :predefinedlocation_id)
    
    Inspection.reset_column_information
    
    Inspection.update_all({:location => ""}, {:location => nil})
    
    change_column(:inspectionsmaster, :location, :string, :null => false)
  end
  
  def self.down
    drop_foreign_key(:inspectionsmaster, :predefinedlocations, :source_column => :predefinedlocation_id)
    remove_column(:inspectionsmaster, :predefinedlocation_id)
    change_column(:inspectionsmaster, :location, :string, :null => true)
  end
end