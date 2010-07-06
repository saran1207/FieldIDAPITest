require "setup_data_last_mod_dates"

class AddLocationsToSetupDataLastModDates < ActiveRecord::Migration
  
  def self.up
     add_column(:setupdatalastmoddates, :locations, :datetime, :null=> false)
     SetupDataLastModDates.reset_column_information
     SetupDataLastModDates.update_all("locations = '2010-01-01 00:00:00'")
  end
  
  def self.down
    remove_column(:setupdatalastmoddates, :locations)
    
  end
  
  
end