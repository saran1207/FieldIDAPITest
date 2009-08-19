require "tenant"
require "user"
require "addressinfo"

class BaseOrg < ActiveRecord::Base
  set_table_name :org_base
  
  belongs_to  :tenant,      :foreign_key => 'tenant_id',      :class_name => 'Tenant'
  belongs_to  :modifiedBy,  :foreign_key => 'modifiedby',     :class_name => 'User'
  belongs_to  :addressInfo, :foreign_key => 'addressinfo_id', :class_name => 'Addressinfo'
  
  def displayString
    "#{name} (#{id.to_s})"
  end
end