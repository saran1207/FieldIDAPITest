require "user"

class AddStateToUsers < ActiveRecord::Migration

	def self.up
		add_column(:users, :state, :string, :null => false)
     	User.reset_column_information
     	User.update_all("state = 'ACTIVE'");
     	execute "UPDATE users SET state ='ARCHIVED', userid = NULL WHERE deleted=true"
     	remove_column(:users, :deleted)
     	remove_column(:users, :archiveduserid)
     	rename_column(:users, :active, :registered)
     end
	
	def self.down
		add_column(:users, :archiveduserid, :string)
		add_column(:users, :deleted, :boolean)
		execute "UPDATE users SET deleted = true WHERE state='ARCHIVED'"
		remove_column(:users, :state)
		rename_column(:users, :registered, :active)
	end

end