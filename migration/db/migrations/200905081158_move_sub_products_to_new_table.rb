require "sub_product"
require "products_product"
class MoveSubProductsToNewTable < ActiveRecord::Migration
  def self.up
    old_sub_products = ProductsProduct.find(:all)
    for old_sub_product in old_sub_products 
      sub_product = SubProduct.new(:product_id => old_sub_product.subproducts_id, :masterproduct_id => old_sub_product.products_id, :label => old_sub_product.mapkey,
        :created => Time.now, :modified => Time.now)
      sub_product.save
    end
    
    drop_table :products_products
  end
  
  def self.down
    SubProduct.delete_all
  end
end