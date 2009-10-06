require 'primary_org'

class AddAutoPublishToPrimary < ActiveRecord::Migration
	def self.up
	    add_column(:org_primary, :autopublish, :boolean)
	    
		PrimaryOrg.find(:all).each do |primary|
			primary.autopublish = false
			primary.save()
		end	    
	end
	
	def self.down
		remove_column(:org_primary, :autopublish)
	end
end