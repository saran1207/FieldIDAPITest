require 'composite_primary_keys'
require 'product_type'
require 'file_attachment'

class ProductTypeFileAttachment < ActiveRecord::Base
  set_primary_keys :producttypes_id, :attachments_id
  set_table_name :producttypes_fileattachments
  
  belongs_to :productType,  :foreign_key => 'producttypes_id',  :class_name => 'ProductType'
  belongs_to :attachment,   :foreign_key => 'attachments_id',   :class_name => 'FileAttachment'
  
end