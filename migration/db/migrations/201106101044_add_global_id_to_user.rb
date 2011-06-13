require "user"

class AddGlobalIdToUser < ActiveRecord::Migration
  
 def self.up
    add_column(:users, :globalId, :string, :limit => 36)
    add_index(:users, :globalId, :unique => true)
  end

  def self.down
    remove_column(:users, :globalId);
  end

end