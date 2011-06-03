require "base_org"
require "primary_org"

class MoveNotesToBaseOrg < ActiveRecord::Migration
  
  def self.up
     add_column(:org_base, :notes, 'varchar(1000)')
   	 BaseOrg.reset_column_information
     BaseOrg.update_all("notes = null")
     
     PrimaryOrg.find(:all, :conditions => "notes IS NOT NULL").each do |org|
       base_org = BaseOrg.find(org.id)
       base_org.notes = org.notes
       base_org.save
     end
          
     remove_column(:org_primary, :notes)
  end

  def self.down
    add_column(:org_primary, :notes, 'varchar(1000)')
   	PrimaryOrg.reset_column_information
    PrimaryOrg.update_all("notes = null");
  
  	BaseOrg.find(:all, :conditions => "notes IS NOT NULL").each do |org|
  	   primary_org = PrimaryOrg.find(org.id)
       primary_org.notes = org.notes
       primary_org.save
     end
  	
    remove_column(:org_base, :notes)
  end

end