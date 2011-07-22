require "tenant"

class TenantSettings < ActiveRecord::Base
	set_table_name :tenant_settings
	
	belongs_to :tenant, :foreign_key => 'tenant_id', :class_name => 'Tenant'
end