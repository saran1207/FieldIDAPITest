class RemoveAlertStatusTable < ActiveRecord::Migration

  def self.up
  	drop_table(:alertstatus)
  end

  def self.down
  end

end
