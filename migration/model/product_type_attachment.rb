require 'product_type'

class ProductTypeAttachment < ActiveRecord::Base
  set_primary_keys :producttypes_id, :element
  set_table_name :producttypes_attachments
  
  belongs_to    :productType,   :foreign_key => 'producttypes_id',    :class_name => 'ProductType'
  
end