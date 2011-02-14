class UpdateAssetColumnSizes < ActiveRecord::Migration
  def self.up
  		change_column(:assets, :rfidnumber, :string, {:limit => 50, :null => true})
  		change_column(:assets, :comments, :string, {:limit => 3000, :null => true})
  		change_column(:assets, :customerrefnumber, :string, {:limit => 300, :null => true})
  end
  
  def self.down
  		change_column(:assets, :rfidnumber, :string, {:limit => 46, :null => true})
  		change_column(:assets, :comments, :string, {:limit => 2047, :null => true})
  		change_column(:assets, :customerrefnumber, :string, {:limit => 255, :null => true})   	
  end
end