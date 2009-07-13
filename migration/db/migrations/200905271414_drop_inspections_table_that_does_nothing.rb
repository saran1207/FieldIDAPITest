class DropInspectionsTableThatDoesNothing < ActiveRecord::Migration
  
  def self.up
    
    drop_table :inspections_inspections
     
  end
  
  def self.down
  end
  
end