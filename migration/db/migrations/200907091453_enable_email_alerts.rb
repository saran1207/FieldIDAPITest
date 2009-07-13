require "organization"
require "organization_extendedfeature"

class EnableEmailAlerts < ActiveRecord::Migration
  def self.up

    tenants = Organization.find(:all, :conditions => {:type => "ORGANIZATION"})
    
    tenants.each do |tenant|
      OrganizationExtendedfeature.create(:organization_id => tenant.id, :element => "EmailAlerts")
    end
    
  end
  
  def self.down
  end
end