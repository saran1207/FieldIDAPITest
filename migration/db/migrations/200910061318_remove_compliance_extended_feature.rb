require "org_extendedfeature"

class RemoveComplianceExtendedFeature < ActiveRecord::Migration
	def self.up
		OrgExtendedfeature.find(:all, :conditions => "feature='Compliance'").each do |complianceFeature|
			complianceFeature.delete()
		end
	end
	
	def self.down
	end
end