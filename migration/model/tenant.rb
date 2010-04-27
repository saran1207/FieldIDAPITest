require "base_org"
require "order"
class Tenant < ActiveRecord::Base
  set_table_name :tenants
   has_many  :orders,          :foreign_key => 'tenant_id',           :class_name => 'Order'
  
  def displayString
    "#{name} (#{id.to_s})"
  end
end