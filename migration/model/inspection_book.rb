require "tenant"
require "user"
require "inspectionmaster"

class InspectionBook < ActiveRecord::Base
  set_table_name :inspectionbooks

  belongs_to  :tenant,        :foreign_key => 'tenant_id',           :class_name => 'Tenant'
  belongs_to  :modifiedBy,    :foreign_key => 'modifiedby',         :class_name => 'User'
  has_many    :inspections,   :foreign_key => 'book_id',            :class_name => 'Inspection'
  
  
  
  def displayString
    "#{name} (#{id.to_s})"
  end
end
