require 'composite_primary_keys'
require 'inspection_type'

class InspectionTypeInfoFieldName < ActiveRecord::Base
  set_primary_keys :inspectiontypes_id, :orderidx
  set_table_name :inspectiontypes_infofieldnames
  
  belongs_to :inspection_type,    :foreign_key => 'inspectiontypes_id',   :class_name => 'InspectionType'
  
end