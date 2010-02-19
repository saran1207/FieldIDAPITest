require 'product'

class SubProduct < ActiveRecord::Base
  set_table_name :subproducts
  belongs_to  :masterProduct,	:foreign_key => 'masterproduct_id',	:class_name => 'Product'
  
end