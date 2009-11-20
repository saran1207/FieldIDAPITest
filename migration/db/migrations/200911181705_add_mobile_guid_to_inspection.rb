class AddMobileGuidToInspection < ActiveRecord::Migration
  def self.up
    add_column :inspections, :mobileguid, :string
  end
  
  def self.down
    remove_column :inspections, :mobileguid
  end
end