require "asset_status"

class AddStateToAssetStatus < ActiveRecord::Migration

	def self.up
		add_column(:assetstatus, :state, :string, :null => false)
     	AssetStatus.reset_column_information
     	AssetStatus.update_all("state = 'ACTIVE'");
     end
	
	def self.down
		remove_column(:assetstatus, :state)
	end

end