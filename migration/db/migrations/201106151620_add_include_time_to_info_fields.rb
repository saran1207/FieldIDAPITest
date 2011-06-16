require "info_field"

class AddIncludeTimeToInfoFields < ActiveRecord::Migration

  def self.up
       add_column(:infofield, :includetime, :boolean)
       InfoField.reset_column_information
       InfoField.update_all(:includetime => false )
  end
  
  def self.down    
	remove_column(:infofield, :includetime)
  end
  
end