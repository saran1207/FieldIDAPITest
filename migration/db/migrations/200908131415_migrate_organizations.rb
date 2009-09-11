require "tenant"
require "organization"
require "base_org"
require "primary_org"
require "secondary_org"
require "org_extendedfeature"
require "organization_extendedfeature"

class MigrateOrganizations < ActiveRecord::Migration
  
  def self.up
    Tenant.transaction do
        
      Organization.find(:all, :conditions => "parent_id is null").each do |tenantOrg|
        tenant = Tenant.new
        tenant.id = tenantOrg.id
        tenant.name = tenantOrg.name
        tenant.save
        
        basePrimary = BaseOrg.new
        basePrimary.id = tenantOrg.id
        basePrimary.tenant = tenant
        basePrimary.created = tenantOrg.created
        basePrimary.modified = tenantOrg.modified
        basePrimary.modifiedby = tenantOrg.modifiedby
        basePrimary.name = tenantOrg.displayname
        basePrimary.addressinfo_id = tenantOrg.r_addressinfo
        basePrimary.save
        
        orgPrimary = PrimaryOrg.new
        orgPrimary.baseOrg = basePrimary
        orgPrimary.defaulttimezone = "United States:New York - New York"
        orgPrimary.asset_limit = tenantOrg.asset_limit
        orgPrimary.diskspace_limit = tenantOrg.diskspace_limit
        orgPrimary.user_limit = tenantOrg.user_limit
        orgPrimary.certificatename = (tenantOrg.certificatename.nil? || tenantOrg.certificatename.strip.empty?) ? nil : tenantOrg.certificatename.strip
        orgPrimary.dateformat = tenantOrg.dateformat
        orgPrimary.serialnumberformat = tenantOrg.serialnumberformat.nil? ? "" : tenantOrg.serialnumberformat
        orgPrimary.usingserialnumber = tenantOrg.usingserialnumber
        orgPrimary.website = tenantOrg.website
        orgPrimary.save
      end
      
      Organization.find(:all, :conditions => "parent_id is not null").each do |tenantOrg|
        orgPrimary = PrimaryOrg.find(tenantOrg.parent_id)
        
        baseSecondary = BaseOrg.new
        baseSecondary.id = tenantOrg.id
        baseSecondary.tenant = orgPrimary.baseOrg.tenant
        baseSecondary.created = tenantOrg.created
        baseSecondary.modified = tenantOrg.modified
        baseSecondary.modifiedby = tenantOrg.modifiedby
        baseSecondary.name = tenantOrg.displayname
        baseSecondary.addressinfo_id = tenantOrg.r_addressinfo
        baseSecondary.save
        
        orgSecondary = SecondaryOrg.new
        orgSecondary.baseOrg = baseSecondary
        orgSecondary.defaulttimezone = "United States:New York - New York"
        orgSecondary.certificatename = (tenantOrg.certificatename.nil? || tenantOrg.certificatename.strip.empty?) ? nil : tenantOrg.certificatename.strip
        orgSecondary.primaryOrg = orgPrimary
        orgSecondary.save
      end
      
      OrganizationExtendedfeature.find(:all).each do |extFeature|
        orgExtFeature = OrgExtendedfeature.new
        orgExtFeature.org_id = extFeature.organization_id
        orgExtFeature.feature = extFeature.element
        orgExtFeature.save
      end
      
    end

  end
  
  def self.down
    execute("delete from orgs_extendedfeatures")
    execute("delete from org_secondary")
    execute("delete from org_primary")
    execute("delete from org_base")
    execute("delete from tenants")
  end
  
end