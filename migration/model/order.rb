require "tenant"
require "user"
require "line_item"

class Order < ActiveRecord::Base
  set_table_name :orders
  
  belongs_to  :tenant,          :foreign_key => 'tenant_id',           :class_name => 'Tenant'
  belongs_to  :modifiedBy,      :foreign_key => 'modifiedby',         :class_name => 'User'
  has_many    :lineItems,       :foreign_key => 'order_id',           :class_name => 'LineItem'
  
  
  
  def displayString
    "#{ordernumber} (#{id.to_s})"
  end
end
