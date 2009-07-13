class RemoveOldChargeField < ActiveRecord::Migration
  def self.up
     remove_column(:inspectionsmaster, :charge)   
  end
  
  def self.down
  end
end