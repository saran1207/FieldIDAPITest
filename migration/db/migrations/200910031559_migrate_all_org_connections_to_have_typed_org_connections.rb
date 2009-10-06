require "org_connection"
require "typed_org_connection"
require "base_org"
class MigrateAllOrgConnectionsToHaveTypedOrgConnections < ActiveRecord::Migration
  def self.up
    OrgConnection.find_each do |c|
      vendor = BaseOrg.find(c.vendor_id)
      custOrg = TypedOrgConnection.new(:owner_id => c.vendor_id, :tenant_id => vendor.tenant_id, :connectedorg_id => c.customer_id, 
            :orgconnection_id => c.id, :connectiontype => "CUSTOMER", :created => Time.now, :modified => Time.now)
      custOrg.save
      
      customer = BaseOrg.find(c.customer_id)
      vendorOrg = TypedOrgConnection.new(:owner_id => c.customer_id, :tenant_id => customer.tenant_id, :connectedorg_id => c.vendor_id, 
            :orgconnection_id => c.id, :connectiontype => "VENDOR", :created => Time.now, :modified => Time.now)
      vendorOrg.save
    end
  end
  
  def self.down
  end
end