require 'setup_data_last_mod_dates'
class CreateDefaultSetupDataLastModDates < ActiveRecord::Migration
  
  def self.up
    now = Time.now
    
    tenants = Organization.find(:all, :conditions => "parent_id is null")
    
    tenants.each do |tenant|
      puts "Tenant: " + tenant.name
      SetupDataLastModDates.create(
        :tenant => tenant, 
        :producttypes => now,
        :inspectiontypes => now,
        :autoattributes => now,
        :owners => now
      )
    end
    
  end
  
  def self.down
    execute("DELETE FROM setupdatalastmoddates")
  end
end