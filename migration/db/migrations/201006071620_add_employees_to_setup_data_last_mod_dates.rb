require "setup_data_last_mod_dates"

class AddEmployeesToSetupDataLastModDates < ActiveRecord::Migration
  
  def self.up
     add_column(:setupdatalastmoddates, :employees, :datetime, :null=> false)
     SetupDataLastModDates.reset_column_information
     SetupDataLastModDates.update_all("employees = '2010-06-07 00:00:00'")
  end
  
  def self.down
    remove_column(:setupdatalastmoddates, :employees)
    
  end
  
  
end