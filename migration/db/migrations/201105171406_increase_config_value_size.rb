class IncreaseConfigValueSize < ActiveRecord::Migration
  def self.up
  		change_column(:configurations, :value, :string, {:limit => 512})
  end
  
  def self.down
  		change_column(:configurations, :value, :string, {:limit => 255})
  end
end