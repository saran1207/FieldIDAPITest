require "customer"
require "user"
require "addressinfo"
require "tenant"

class Division < ActiveRecord::Base
  set_table_name :divisions
  
  belongs_to  :tenant,      :foreign_key => 'tenant_id',       :class_name => 'Tenant'
  belongs_to  :customer,    :foreign_key => 'customer_id',  :class_name => 'Customer'
  belongs_to  :addressinfo, :foreign_key => 'addressinfo_id', :class_name => 'Addressinfo'
  belongs_to  :modifiedBy,  :foreign_key => 'modifiedby',   :class_name => 'User'
  
  def displayString
    "#{name} (#{id.to_s})"
  end  
end