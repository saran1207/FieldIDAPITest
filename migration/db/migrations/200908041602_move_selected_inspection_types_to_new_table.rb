require "product_type"
require "associated_inspection_type"
class MoveSelectedInspectionTypesToNewTable < ActiveRecord::Migration
  def self.up
    ProductType.find_each(:conditions => "state = 'ACTIVE'") do |product_type| 
      for inspection_type in product_type.inspectionTypes
        associated_inspection_type = AssociatedInspectionType.new
        associated_inspection_type.producttype_id = product_type.id
        associated_inspection_type.inspectiontype_id = inspection_type.id
        associated_inspection_type.created = Time.new
        associated_inspection_type.modified = Time.new
        associated_inspection_type.save
      end
    end
    drop_table(:producttypes_inspectiontypes)
  end
  
  def self.down
  end
end
