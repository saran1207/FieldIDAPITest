require "add_product_history"
class UpdateAddproducthistoryLocationFieldsToEmptyStringIfNull < ActiveRecord::Migration
  def self.up
       AddProductHistory.reset_column_information
       AddProductHistory.update_all({:location=>""},  { :location => nil })
       change_column(:addproducthistory, :location, :string, :null => false)
  end

  def self.down
      change_column(:addproducthistory, :location, :string, :null => true)
  end
end
    
