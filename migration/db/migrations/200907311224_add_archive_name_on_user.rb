require "user"
class AddArchiveNameOnUser < ActiveRecord::Migration
  
  def self.up
    add_column(:users, :archiveduserid, :string, :limit => 15)    
    User.reset_column_information
  end
  
  def self.down
    remove_column(:users, :archiveduserid)
  end
  
end