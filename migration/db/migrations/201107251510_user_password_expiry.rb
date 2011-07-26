class UserPasswordExpiry < ActiveRecord::Migration

  def self.up  
	add_column(:users, :passwordExpiry, :datetime, :null=> true)
  end
  
  def self.down    
	remove_column(:users, :passwordExpiry)
  end
  
end