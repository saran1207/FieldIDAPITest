require "tenant"
require "primary_org"
require "base_org"
require "typed_org_connection"
class AddFieldidAsACatalogOnlyConnectionToEveryone < ActiveRecord::Migration
  def self.up
    fieldid = Tenant.find(:first, :conditions => { :name => "fieldid" })
    
    fieldid_primary_org = BaseOrg.find(:first, :conditions => { :tenant_id => fieldid.id })
    
    PrimaryOrg.find_each do |primary_org|
      if primary_org.id != fieldid_primary_org
        catalogConnection = TypedOrgConnection.new(:owner_id => primary_org.org_id, :tenant_id => primary_org.baseOrg.tenant_id, :connectedorg_id => fieldid_primary_org.id, 
                                                   :connectiontype => "CATALOG_ONLY", :created => Time.now, :modified => Time.now)
        catalogConnection.save
      end
    end
    
  
  end
  
  def self.down
  end
end