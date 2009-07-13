require "organization"
require "user"
require "product_type"
require "customer"
require "inspection_type"

class ProductTypeSchedule < ActiveRecord::Base
  set_table_name :producttypeschedules
  
  belongs_to  :tenant,          :foreign_key => 'r_tenant',             :class_name => 'Organization'
  belongs_to  :modifiedBy,      :foreign_key => 'modifiedby',           :class_name => 'User'
  belongs_to  :productType,     :foreign_key => 'producttype_uniqueid', :class_name => 'ProductType'
  belongs_to  :customer,        :foreign_key => 'customer_uniqueid',    :class_name => 'Customer'
  belongs_to  :inspectionType,  :foreign_key => 'inspectiontype_id',    :class_name => 'InspectionType'

  def displayString
    "#{frequency.to_s} (#{id.to_s})"
  end
end