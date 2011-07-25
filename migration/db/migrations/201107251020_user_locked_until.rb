class UserLockedUntil < ActiveRecord::Migration

  def self.up  
	add_column(:users, :lockedUntil, :datetime, :null=> true)
  end
  
  def self.down    
	remove_column(:users, :lockedUntil)
  end
  
end