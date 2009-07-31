class AddArchiveNameOnUser < ActiveRecord::Migration
  
  def self.up
    add_column(:users, :archiveduserid, :string, :limit => 15)    
  end
  
  def self.down
    remove_column(:users, :archiveduserid)
  end
  
end