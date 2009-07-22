require 'setup_data_last_mod_dates'
class CreateJobsDefaultSetupDate < ActiveRecord::Migration
  
  def self.up
  
	setupData = SetupDataLastModDates.find(:all)
	
	setupData.each do |setupDate|
		setupDate.jobs = Time.now
	 	setupDate.save
	end
  end
  
  def self.down
  end
end
	