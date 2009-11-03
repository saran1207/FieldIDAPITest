require "setup_data_last_mod_dates"
class UpdateSetupDataUpdateDates < ActiveRecord::Migration
  def self.up
    SetupDataLastModDates.reset_column_information
    
    now = Time.new.utc
    SetupDataLastModDates.update_all({:producttypes => now, :owners => now, :jobs => now})
  end
end