class AddJobsToSetupData < ActiveRecord::Migration
  def self.up
    add_column(:setupdatalastmoddates, :jobs, :timestamp)
  end
  
  def self.down
  	drop_column(:setupdatalastmoddates, :jobs)
  end
end
