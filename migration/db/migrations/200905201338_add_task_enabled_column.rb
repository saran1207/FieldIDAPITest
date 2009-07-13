class AddTaskEnabledColumn < ActiveRecord::Migration
  def self.up

    add_column(:tasks, :enabled, :boolean, :null => false)
    
  end
  
  def self.down
    
    remove_column(:tasks, :enabled)
    
  end
end