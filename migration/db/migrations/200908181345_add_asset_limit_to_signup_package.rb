class AddAssetLimitToSignupPackage < ActiveRecord::Migration	
	def self.up
		add_column(:signuppackage, :asset_limit, :integer)
	end
	
	def self.down
		remove_column(:signuppackage, :asset_limit, :integer)
	end
end