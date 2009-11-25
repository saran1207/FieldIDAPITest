require "setup_data_last_mod_dates"

class TouchProductTypesSetupLastModDates < ActiveRecord::Migration
	def self.up
		SetupDataLastModDates.find(:all).each do |setupDataLastModDate|
			setupDataLastModDate.producttypes = DateTime.now
			setupDataLastModDate.save
		end
	end
	
	def self.down
	end
end 