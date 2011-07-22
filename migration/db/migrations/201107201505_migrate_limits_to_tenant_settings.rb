require "primary_org"
require "tenant_settings"

class MigrateLimitsToTenantSettings < ActiveRecord::Migration

  def self.up
  	TenantSettings.reset_column_information
  
    PrimaryOrg.find(:all).each do |org|
    	TenantSettings.create(
    		:created => Time.new,
    		:modified => Time.new,
    		:tenant_id => org.baseOrg.tenant.id,
    		:maxEmployeeUsers => org.user_limit, 
    		:maxLiteUsers => org.lite_user_limit, 
    		:maxReadonlyUsers => org.readonly_user_limit, 
    		:secondaryOrgsEnabled => org.org_limit != 0
    	)
    end
  end

  def self.down
  end

end
