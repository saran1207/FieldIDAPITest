
  
  
require "abstract_inspection"

class SubInspection < ActiveRecord::Base
  set_table_name :inspectionssub
  
  belongs_to  :inspection,      :foreign_key => 'inspection_id',      :class_name => 'AbstractInspection'
end