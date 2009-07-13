class LowercaseTenantName < ActiveRecord::Migration
	def self.up
		execute "update organization set name=lower(name)"
	end
end