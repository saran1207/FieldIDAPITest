require "user"

class AddReferralUserColumn < ActiveRecord::Migration
  def self.up
  	add_column(:users, :referralkey, :string, :limit => 10)
  	add_index(:users, :referralkey, :unique => true)
  end
  
  def self.down
   	remove_column(:users, :referralkey)
  end
end