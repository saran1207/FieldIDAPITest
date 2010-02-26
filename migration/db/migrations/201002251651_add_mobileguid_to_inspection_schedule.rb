class AddMobileguidToInspectionSchedule < ActiveRecord::Migration
  def self.up
    add_column :inspectionschedules, :mobileguid, :string
  end
  
  def self.down
    remove_column :inspectionschedules, :mobileguid
  end
  
end
