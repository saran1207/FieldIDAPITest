class AddProductStatusToInspection < ActiveRecord::Migration
  def self.up
  	add_column(:inspections, :productstatus_id, :integer)
  	add_foreign_key(:inspections, :productstatus, :source_column => :productstatus_id, :foreign_column => :uniqueid, :name => "fk_inspections_productstatus")
  end
  
  def self.down
  	remove_column(:inspections, :productstatus_id)
  end
end