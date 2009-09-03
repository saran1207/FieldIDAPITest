class FixAssetLimitColumnNameOnPromoCode < ActiveRecord::Migration

	def self.up	
	    remove_column(:promocodes, :assets_limit)
	    add_column(:promocodes, :asset_limit, :integer)
	    execute "UPDATE promocodes set asset_limit = 0";		
	end
	
	def self.down
	    remove_column(:promocodes, :asset_limit)
	    add_column(:promocodes, :assets_limit, :integer)
	    execute "UPDATE promocodes set assets_limit = 0";		
	end

end