require "base_org"

class Tenant < ActiveRecord::Base
  set_table_name :tenants
  
  has_one :primaryOrg, :foreign_key => 'tenant_id', :class_name => 'BaseOrg', :readonly => true
  
  def displayString
    "#{name} (#{id.to_s})"
  end
end