class AddNotNullConstraintOwnerOnProductAndInspection < ActiveRecord::Migration
  def self.up
    change_column(:products, :owner_id, :integer, :null => false)
    change_column(:inspectionsmaster, :owner_id, :integer, :null => false)
  end
  
  def self.down
  end
end