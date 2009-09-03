class FixUserLimitColumnNameOnPromoCode < ActiveRecord::Migration

	def self.up
	    remove_column(:promocodes, :users_limit)
	    add_column(:promocodes, :user_limit, :integer)
	    execute "UPDATE promocodes set user_limit = 0";			    	
	end
	
	def self.down
	    remove_column(:promocodes, :user_limit)
	    add_column(:promocodes, :users_limit, :integer)
	    execute "UPDATE promocodes set users_limit = 0";			    	
	end
	
end