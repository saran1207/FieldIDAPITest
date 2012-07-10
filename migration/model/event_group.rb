require "tenant"
require "user"
#require "inspectionmaster"

class EventGroup < ActiveRecord::Base
  set_table_name :eventgroups

  belongs_to  :tenant,      :foreign_key => 'tenant_id',   :class_name => 'Tenant'
  belongs_to  :createdBy,   :foreign_key => 'createdby', :class_name => 'User'
  belongs_to  :modifiedBy,  :foreign_key => 'modifiedby', :class_name => 'User'
  #has_many    :inspections, :foreign_key => 'book_id',    :class_name => 'Inspection'
  
end
