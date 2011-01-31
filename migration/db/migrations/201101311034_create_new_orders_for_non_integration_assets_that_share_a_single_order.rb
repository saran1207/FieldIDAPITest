require "asset"
require "line_item"
require "tenant"
require "org_extendedfeature"

class CreateNewOrdersForNonIntegrationAssetsThatShareASingleOrder < ActiveRecord::Migration
  def self.up

    #Find all assets which contain duplicate shoporder ids
    @assets = Asset.find_by_sql('SELECT DISTINCT shoporder_id, id, tenant_id from assets group by shoporder_id HAVING count(shoporder_id) > 1')

    while !@assets.empty?
      @assets.each do |asset|

        #If This asset's tenant doesn't have integration, create a new line item /shop order for it.
        if OrgExtendedfeature.find(:all, :conditions => {:org_id=> asset.tenant_id, :feature=>'Integration'}).empty?

          oldOrder = Order.find_by_id(asset.shoporder_id)
         
          newOrder = Order.create(:created => Time.now,
            :modified  => Time.now,
            :tenant_id => asset.tenant_id,
            :ordertype => "SHOP",
            :ordernumber=> oldOrder.ordernumber)
        
          LineItem.create(:order_id => newOrder.id,
            :created => Time.now,
            :modified  => Time.now,
            :quantity => 0,
            :assetcode => "DEFAULT",
            :tenant_id => asset.tenant_id ,
            :idx =>0,
            :description=>"")

          Asset.update(asset.id, :shoporder_id => newOrder.id )
        end
      end
      @assets = Asset.find_by_sql('SELECT DISTINCT shoporder_id, id, tenant_id from assets group by shoporder_id HAVING count(shoporder_id) > 1')
    end
  end
end
