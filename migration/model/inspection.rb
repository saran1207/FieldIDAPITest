require "organization"
require "user"
require "inspection_type"
require "inspection_group"
require "inspection_book"
require "inspection_attachment"
require "inspection_info_option"
require "product"
require "customer"
require "division"
require "criteria_result"
require "inspection_file_attachment"

class Inspection < ActiveRecord::Base
  set_table_name :inspections
  
  belongs_to  :tenant,          :foreign_key => 'r_tenant',           :class_name => 'Organization'
  belongs_to  :modifiedBy,      :foreign_key => 'modifiedby',         :class_name => 'User'
  belongs_to  :inspectionType,  :foreign_key => 'type_id',            :class_name => 'InspectionType'
  belongs_to  :group,           :foreign_key => 'group_id',           :class_name => 'InspectionGroup'
  belongs_to  :book,            :foreign_key => 'book_id',            :class_name => 'InspectionBook'
  belongs_to  :inspector,       :foreign_key => 'inspector_uniqueid', :class_name => 'User'
  belongs_to  :product,         :foreign_key => 'product_id',         :class_name => 'Product'
  belongs_to  :customer,        :foreign_key => 'customer_uniqueid',  :class_name => 'Customer'
  belongs_to  :division,        :foreign_key => 'division_uniqueid',  :class_name => 'Division'
  belongs_to  :organization,    :foreign_key => 'organization_id',    :class_name => 'Organization'
  has_many    :results,         :foreign_key => 'inspection_id',      :class_name => 'CriteriaResult'
  has_many    :attachments,     :foreign_key => 'inspections_id',     :class_name => 'InspectionAttachments'
  has_many    :fileAttachments, :foreign_key => 'inspections_id',     :class_name => 'InspectionFileAttachments'
  has_many    :infoOptions,     :foreign_key => 'inspections_id',     :class_name => 'InspectionInfoOption'
  
  def getFilePath
    return tenant.name + created.strftime("/%y/%m/") + id.to_s
  end
  
  def displayString
    "#{date} (#{id.to_s}): Type: #{inspectionType.displayString}, Product: #{product.displayString}"
  end
  
end