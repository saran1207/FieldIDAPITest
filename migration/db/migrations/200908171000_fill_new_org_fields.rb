require "tenant"
require "base_org"
require "primary_org"
require "secondary_org"
require "organization"

class FillNewOrgFields < ActiveRecord::Migration
  
  def self.up
    Tenant.transaction do
      
      execute("update org_primary set defaulttimezone='United States:New York - New York'")
      execute("update org_secondary set defaulttimezone='United States:New York - New York'")
      
      Organization.find(:all, :conditions => "parent_id is null").each do |tenantOrg|
        orgPrimary = PrimaryOrg.find(tenantOrg.id)
        orgPrimary.certificatename = (tenantOrg.certificatename.nil? || tenantOrg.certificatename.strip.empty?) ? tenantOrg.displayname : tenantOrg.certificatename.strip
        orgPrimary.dateformat = (tenantOrg.dateformat.nil?) ? "MM/dd/yy" : tenantOrg.dateformat
        orgPrimary.save
      end
      
      Organization.find(:all, :conditions => "parent_id is not null").each do |orgUnit|
        orgSecondary = SecondaryOrg.find(orgUnit.id)
        orgSecondary.certificatename = (orgUnit.certificatename.nil? || orgUnit.certificatename.strip.empty?) ? orgSecondary.primaryOrg.baseOrg.name : orgUnit.certificatename.strip
        orgSecondary.save
      end
      
      change_column(:org_secondary, :certificatename, :string, :null => false)
      change_column(:org_secondary, :defaulttimezone, :string, :null => false)

      change_column(:org_primary, :certificatename, :string, :null => false)
      change_column(:org_primary, :defaulttimezone, :string, :null => false)
      change_column(:org_primary, :dateformat, :string, :null => false)
      
    end

  end
  
  def self.down
  end
  
end