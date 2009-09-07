require "base_org"
require "secondary_org"

class MigrateSecurityFieldsOnOrgBase < ActiveRecord::Migration
	def self.up
    SecondaryOrg.find(:all).each do |secOrg|
      secOrg.baseOrg.secondary_id = secOrg.id
      secOrg.baseOrg.save
    end
    
  end
	
	def self.down
	end
end