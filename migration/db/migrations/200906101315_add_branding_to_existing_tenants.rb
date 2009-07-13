require "organization"
require "organization_extendedfeature"
class AddBrandingToExistingTenants < ActiveRecord::Migration
  def self.up
    tenants = Organization.find(:all, :conditions => ("id = r_tenant"))
    for tenant in tenants do 
      OrganizationExtendedfeature.new(:organization_id => tenant.id, :element => "Branding").save
    end
  end
  
  def self.down
    OrganizationExtendedfeature.delete_all(:conditions => {:element => "Branding"})
  end
end