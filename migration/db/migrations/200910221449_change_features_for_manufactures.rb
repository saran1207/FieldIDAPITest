require "primary_org"
require "org_extendedfeature"
require "tenant"
class ChangeFeaturesForManufactures < ActiveRecord::Migration
  
  def self.up
    change_to_manufacturer_features 'jergens'
    change_to_manufacturer_features 'msa'
    change_to_manufacturer_features 'seafit'
    change_to_manufacturer_features 'superchute'
    change_to_manufacturer_features 'fieldid'
  end
  
  def self.change_to_manufacturer_features(tenant_name)
    tenant = Tenant.find(:first, :conditions => { :name => tenant_name })
    primary_org = PrimaryOrg.find(:first, :conditions => { :org_id => tenant.id } )
    OrgExtendedfeature.delete_all(["org_id = ? and feature = 'PartnerCenter'", primary_org.id])
    primary_org.asset_limit=-1
    primary_org.save
  end
end