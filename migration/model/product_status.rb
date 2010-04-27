require "product"

class ProductStatus < ActiveRecord::Base
  set_table_name :productstatus
  set_primary_key :uniqueid
  belongs_to :product,   :foreign_key => 'productstatus_uniqueid',     :class_name => 'Product'
end

