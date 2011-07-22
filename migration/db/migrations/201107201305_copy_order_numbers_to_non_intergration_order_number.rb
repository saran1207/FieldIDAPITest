require "tenant"
require "asset"
require "line_item"

class CopyOrderNumbersToNonIntergrationOrderNumber < ActiveRecord::Migration

  def self.up
  	tenants = Tenant.find_by_sql("select * from tenants where id not in(select distinct tenant_id from org_base where id in (select org_id from org_extendedfeatures where feature = 'Integration'));")
  	  		
	say "Total tenants found:"
	say tenants.size, true

  	tenants.each do |tenant|
  		say "Processing tenant:"
  		say tenant.id, true
  		assets = Asset.find(:all, :conditions => ["shoporder_id IS NOT NULL AND tenant_id =:tenantId", {:tenantId => tenant.id}])
  		say "Assets found:"
  		say assets.size, true
  		
  		suppress_messages do	
	  		 assets.each do |asset|
				shoporder = LineItem.find(asset.shoporder_id)
				order = Order.find(shoporder.order_id)
				
				asset.nonIntergrationOrderNumber = order.ordernumber
				asset.save
				
				execute "UPDATE assets SET shoporder_id = null where id = #{asset.id}"
			end	
			
	  		LineItem.delete_all({:tenant_id => tenant.id})
	  	end
  	end  	
 end

  def self.down
  end

end