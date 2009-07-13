class IncreaseProductTypeNameLength < ActiveRecord::Migration
  def self.up
    change_column(:producttypegroups, :name, :string, :limit => 40, :null => false)
  end
  
  def self.down
    change_column(:producttypegroups, :name, :string, :limit => 25, :null => false)
  end
end