class UserAccountPolicy < ActiveRecord::Migration

  def self.up  
  	add_column(:users, :locked, :boolean, { :default => false })
  	add_column(:users, :failedLoginAttempts, :integer, { :default => 0 })
  end
  
  def self.down    
  	remove_column(:users, :locked)
  	remove_column(:users, :failedLoginAttempts)
  end
  
end