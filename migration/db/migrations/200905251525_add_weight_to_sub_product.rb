require "sub_product"
class AddWeightToSubProduct < ActiveRecord::Migration
  def self.up
    add_column(:subproducts, :weight, :integer)
    SubProduct.reset_column_information
    sub_products = SubProduct.find(:all, :order => " masterproduct_id, created, id")
    
    idx = 0
    master_id = 0
    for sub_product in sub_products
      idx = 0 unless sub_product.masterproduct_id == master_id  
      master_id = sub_product.masterproduct_id
      sub_product.weight = idx
      sub_product.save
      idx += 1
    end
    
    change_column(:subproducts, :weight, :integer, :null => false)
  end
  
  def self.down
    remove_column(:subproducts, :weight)
  end
end
