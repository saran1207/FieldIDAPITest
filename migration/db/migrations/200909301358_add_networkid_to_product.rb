require 'product'

class AddNetworkidToProduct < ActiveRecord::Migration
  
  def self.up
    
    add_column(:products, :network_id, :integer)
    add_index(:products, :network_id)
    
    
    Product.find_each do |product|
      product.network_id = product.id
      product.save
    end
    
  end

  def self.down
    remove_column(:products, :network_id)
    
  end

end