require "product"

class FixStandardMasters < ActiveRecord::Migration 
	def self.up
		Product.transaction do
		
			tenantId = 15511521
			productTypeMappings = {898 => 871, 892 => 880, 865 => 886, 888 => 884, 894 => 876}
	
			productTypeMappings.each do |fromTypeId, toTypeId|

				# Find the master product id for fromTypeId where it has a sub-product of the same type
				products = Product.find_by_sql [
					"select 
						pm.id
					from 
						products pm, 
						products ps, 
						subproducts sp    
					where
						pm.tenant_id = ? and
						pm.type_id = ? and
						pm.type_id = ps.type_id and
						sp.masterproduct_id = pm.id and 
						sp.product_id = ps.id", 
					tenantId, 
					fromTypeId
				]
				
				products.each do |prod|
					# switch the product type
					execute("UPDATE products SET type_id = #{toTypeId} WHERE id = #{prod.id}")

					# delete any inspections schedules
					execute("DELETE FROM inspectionschedules WHERE status = 'SCHEDULED' AND product_id = #{prod.id}")
				end
			end
			
		end
	end
	
	def self.down
	end
end