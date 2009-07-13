require "customer"
require "user"

class Division < ActiveRecord::Base
  set_table_name :divisions
  
  belongs_to  :customer,    :foreign_key => 'customer_id',  :class_name => 'Customer'
  belongs_to  :modifiedBy,  :foreign_key => 'modifiedby',   :class_name => 'User'
  
  def displayString
    "#{name} (#{id.to_s})"
  end  
end