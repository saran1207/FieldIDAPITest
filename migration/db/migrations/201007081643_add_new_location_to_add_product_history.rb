require "add_product_history"
class AddNewLocationToAddProductHistory < ActiveRecord::Migration
  def self.up
    add_column(:addproducthistory, :predefinedlocation_id, :integer)
    add_foreign_key(:addproducthistory, :predefinedlocations, :source_column => :predefinedlocation_id, :foreign_column => :id)
  end
  
  def self.down
     remove_column(:addproducthistory, :predefinedlocation_id)
  end
end