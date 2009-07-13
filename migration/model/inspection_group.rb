require "organization"
require "user"
require "inspection"

class InspectionGroup < ActiveRecord::Base
  set_table_name :inspectiongroups

  belongs_to  :tenant,        :foreign_key => 'r_tenant',   :class_name => 'Organization'
  belongs_to  :modifiedBy,    :foreign_key => 'modifiedby', :class_name => 'User'
  has_many    :inspections,   :foreign_key => 'book_id',    :class_name => 'Inspection'
  
end