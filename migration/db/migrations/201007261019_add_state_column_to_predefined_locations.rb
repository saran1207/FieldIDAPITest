require "predefined_location"

class AddStateColumnToPredefinedLocations < ActiveRecord::Migration
  def self.up
     add_column(:predefinedlocations, :state, :string, :null=> false)
     PredefinedLocation.reset_column_information
     PredefinedLocation.update_all("state = 'ACTIVE'");
  end

  def self.down
    remove_column(:predefinedlocations, :state)
  end
end