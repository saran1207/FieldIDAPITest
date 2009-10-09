class AddExternalPasswordAndUserNameToPrimary < ActiveRecord::Migration
	def self.up
		add_column(:org_primary, :externalpassword, :string)
		add_column(:org_primary, :externalusername, :string)	
    PrimaryOrg.reset_column_information  
	end
	
	def self.down
		remove_column(:org_primary, :externalpassword)
		remove_column(:org_primary, :externalusername)
    PrimaryOrg.reset_column_information  
	end
end