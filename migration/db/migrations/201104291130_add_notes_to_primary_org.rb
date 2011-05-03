require "primary_org"

class AddNotesToPrimaryOrg < ActiveRecord::Migration
  
  def self.up
     add_column(:org_primary, :notes, 'varchar(1000)')
   	 PrimaryOrg.reset_column_information
     PrimaryOrg.update_all("notes = null");
  end

  def self.down
    remove_column(:org_primary, :notes)
  end

end