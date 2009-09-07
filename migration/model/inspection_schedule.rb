require "organization"
require "user"
require "product"
require "inspection_type"
require "inspectionmaster"

class InspectionSchedule < ActiveRecord::Base
  set_table_name :inspectionschedules
  
  belongs_to  :tenant,          :foreign_key => 'tenant_id',           :class_name => 'Tenant'
  belongs_to  :modifiedBy,      :foreign_key => 'modifiedby',         :class_name => 'User'
  belongs_to  :product,         :foreign_key => 'product_id',         :class_name => 'Product'
  belongs_to  :inspectionType,  :foreign_key => 'inspectiontype_id',  :class_name => 'InspectionType'
  belongs_to  :inspection,      :foreign_key => 'inspection_inspection_id',      :class_name => 'Inspection'
  
  def displayString
    "#{nextdate.to_s} (#{id.to_s})"
  end
end
