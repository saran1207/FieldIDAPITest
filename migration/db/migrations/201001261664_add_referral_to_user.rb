require "java"
require "FieldidUtils.jar"
require "user"

class AddReferralToUser < ActiveRecord::Migration
  def self.up
  	Tenant.transaction do
  		rs = com.n4systems.util.RandomString.new(10)

  		User.find_each do |user|
  			user.referralkey = rs.next()
  			user.save
  		end

  		change_column(:users, :referralkey, :string, {:limit => 10, :null => false})
  	end
  end
  
  def self.down
   	
  end
end