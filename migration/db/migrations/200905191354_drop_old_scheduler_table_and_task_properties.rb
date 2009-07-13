class DropOldSchedulerTableAndTaskProperties < ActiveRecord::Migration
  def self.up

    drop_table(:schedulerjob)
    drop_table(:tasks_property)
    add_column(:tasks, :taskEntityId, :integer)
    
  end
  
  def self.down
  end
end