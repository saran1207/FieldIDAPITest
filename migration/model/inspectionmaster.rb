require "abstract_inspection"
require "organization"
require "user"
require "inspection_group"
require "inspection_book"
require "customer"
require "division"
require "inspection_file_attachment"

class Inspection < ActiveRecord::Base
  set_table_name :inspectionsmaster
  set_primary_key :inspection_id
  
  belongs_to  :inspection,      :foreign_key => 'inspection_id',      :class_name => 'AbstractInspection'
  belongs_to  :group,           :foreign_key => 'group_id',           :class_name => 'InspectionGroup'
  belongs_to  :book,            :foreign_key => 'book_id',            :class_name => 'InspectionBook'
  belongs_to  :inspector,       :foreign_key => 'inspector_uniqueid', :class_name => 'User'
  
  belongs_to  :customer,        :foreign_key => 'customer_id',  :class_name => 'Customer'
  belongs_to  :division,        :foreign_key => 'division_id',  :class_name => 'Division'
  belongs_to  :organization,    :foreign_key => 'organization_id',    :class_name => 'Organization'  
  
end