require 'composite_primary_keys'
require "product_type"
require "inspection_type"

class ProductTypeInspectionType < ActiveRecord::Base
  set_table_name :producttypes_inspectiontypes
  set_primary_keys :producttypes_id, :inspectiontypes_id
  
  belongs_to  :inspectionType,  :foreign_key => 'inspectiontypes_id',    :class_name => 'InspectionType'
  belongs_to  :productType,     :foreign_key => 'producttypes_id',  :class_name => 'ProductType'
  
end