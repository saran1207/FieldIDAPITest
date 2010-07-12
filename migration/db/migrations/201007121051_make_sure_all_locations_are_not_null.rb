require 'product'
class MakeSureAllLocationsAreNotNull < ActiveRecord::Migration
  def self.up 
    Product.reset_column_information
    Product.update_all({:location=>""},  { :location => nil })
    
    change_column(:products, :location, :string, :null => false)
    
  end
  
  def self.down
     change_column(:products, :location, :string, :null => true)
  end
end