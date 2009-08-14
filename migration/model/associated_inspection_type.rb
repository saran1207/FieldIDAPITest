class AssociatedInspectionType < ActiveRecord::Base
  set_table_name :associatedinspectiontypes
  
   belongs_to  :inspection_type,                :foreign_key => 'inspectiontype_id',           :class_name => 'InspectionType'
end