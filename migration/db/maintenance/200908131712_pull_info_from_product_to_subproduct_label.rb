require "product"

class PullInfoFromProductToSubproductLabel < ActiveRecord::Migration

	def self.up
		
		piedmont = 15511520
		
		products = Product.find(:all, :conditions => { :r_tenant => piedmont })
		
		for product in products
			for subProduct in product.subProducts 
				
				if (product.serialnumber.include? "NSA")
					subProduct.label = ""
				else
					subProduct.label = product.serialnumber
					
					if (product.customerrefnumber != nil)
						subProduct.label += " - "
					end					
				end

				if (product.customerrefnumber != nil)
					subProduct.label += product.customerrefnumber
				end
				
				subProduct.save
			end
		end
	end
	
	def self.down
		
		piedmont = 15511520
		
		products = Product.find(:all, :conditions => { :r_tenant => piedmont })
		
		for product in products
			for subProduct in product.subProducts 
				subProduct.label = ""
				subProduct.save
			end
		end
	end
	
end
		