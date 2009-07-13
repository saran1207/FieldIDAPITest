require 'composite_primary_keys'
require 'inspection'
require 'file_attachment'

class InspectionFileAttachment < ActiveRecord::Base
  set_primary_keys :inspections_id, :attachments_id
  set_table_name :inspections_fileattachments
  
  belongs_to :inspection, :foreign_key => 'inspections_id', :class_name => 'Inspection'
  belongs_to :attachment, :foreign_key => 'attachments_id', :class_name => 'FileAttachment'
  
end