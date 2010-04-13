require 'abstract_inspection'

class InspectionAttachment < ActiveRecord::Base
  set_primary_keys :inspections_id, :element
  set_table_name :inspections_attachments
  
  belongs_to    :inspection,   :foreign_key => 'inspections_id',    :class_name => 'AbstractInspection'
  
end