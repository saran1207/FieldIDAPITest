require 'eventbook'
class MakeEventbooksArchivable < ActiveRecord::Migration

	def self.up
		add_column(:eventbooks, :state, :string, :null=> false)
		EventBook.update_all("state = 'ACTIVE'")
	end
	
	def self.down
		remove_column(:eventbooks, :state)
	end

end