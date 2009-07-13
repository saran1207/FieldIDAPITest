require "product"
class AddSubProductTable < ActiveRecord::Migration
  def self.up
    create_table :subproducts do |t|
      create_abstract_entity_fields_on(t)
      t.integer :product_id, :null => false
      t.integer :masterproduct_id, :null => false
      t.string :label
    end
    foreign_key(:subproducts, :product_id, :products, :id)
    foreign_key(:subproducts, :masterproduct_id, :products, :id)
    
    remove_index(:subproducts, :product_id)
    
    
  end
  
  def self.down
    drop_table :subproducts
  end
end