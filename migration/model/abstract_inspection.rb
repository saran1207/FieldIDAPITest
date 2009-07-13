require "organization"
require "inspection_type"
require "inspection_info_option"
require "product"
require "criteria_result"
require "inspection_attachment"


class AbstractInspection < ActiveRecord::Base
	set_table_name :inspections
  
  belongs_to  :tenant,          :foreign_key => 'r_tenant',           :class_name => 'Organization'
  belongs_to  :modifiedBy,      :foreign_key => 'modifiedby',         :class_name => 'User'
  belongs_to  :inspectionType,  :foreign_key => 'type_id',            :class_name => 'InspectionType'
  belongs_to  :product,         :foreign_key => 'product_id',         :class_name => 'Product'
  has_many    :results,         :foreign_key => 'inspection_id',      :class_name => 'CriteriaResult'
  has_many    :infoOptions,     :foreign_key => 'inspections_id',     :class_name => 'InspectionInfoOption'
  
  def getFilePath
    return tenant.name + created.strftime("/%y/%m/") + id.to_s
  end
  
  def displayString
    "#{date} (#{id.to_s}): Type: #{inspectionType.displayString}, Product: #{product.displayString}"
  end
  
end