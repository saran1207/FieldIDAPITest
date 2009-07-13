require 'composite_primary_keys'
require 'inspection'

class InspectionInfoOption < ActiveRecord::Base
  set_primary_keys :inspections_id, :mapkey
  set_table_name :inspections_infooptionmap
  
  belongs_to :inspection,    :foreign_key => 'inspections_id',   :class_name => 'Inspection'
  
end