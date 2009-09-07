require "add_product_history"

class AddTenantToAddProductHistory < ActiveRecord::Migration
  
	def self.up
    
    add_column(:addproducthistory, :tenant_id, :integer)
    add_foreign_key(:addproducthistory, :tenants,  :source_column => :tenant_id, :foreign_column => :id)

    AddProductHistory.find_each do |aph|
      aph.tenant_id = aph.owner.tenant_id
      aph.save
    end

  end
	
	def self.down
  end
end