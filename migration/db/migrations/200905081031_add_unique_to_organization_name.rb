class AddUniqueToOrganizationName < ActiveRecord::Migration
	def self.up
		add_index(:organization, :name, :unique => true)
	end
	
	def self.down
		remove_index(:organization, :name)
	end
end