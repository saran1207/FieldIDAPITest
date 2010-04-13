require "tenant"
require "user"
require "inspectionsmaster"

class InspectionGroup < ActiveRecord::Base
  set_table_name :inspectiongroups

  belongs_to  :tenant,        :foreign_key => 'tenant_id',   :class_name => 'Tenant'
  belongs_to  :modifiedBy,    :foreign_key => 'modifiedby', :class_name => 'User'
  has_many    :inspections,   :foreign_key => 'book_id',    :class_name => 'Inspection'
  
end
