class IncreaseObservationSize < ActiveRecord::Migration
  def self.up
    change_column(:observations, :text, :string, :limit => 1000, :null => false)
  end
  
  def self.down
  end
end 