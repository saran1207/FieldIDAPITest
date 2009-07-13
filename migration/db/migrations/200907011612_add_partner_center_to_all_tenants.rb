require "organization"
require "organization_extendedfeature"
class AddPartnerCenterToAllTenants < ActiveRecord::Migration
  def self.up
    tenants = Organization.find(:all, :conditions => ("id = r_tenant"))
    for tenant in tenants do 
      OrganizationExtendedfeature.new(:organization_id => tenant.id, :element => "PartnerCenter").save
    end
  end
  
  def self.down
    OrganizationExtendedfeature.delete_all(:conditions => {:element => "PartnerCenter"})
  end
end