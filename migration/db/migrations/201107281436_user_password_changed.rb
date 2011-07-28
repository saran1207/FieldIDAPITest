class UserPasswordChanged < ActiveRecord::Migration

  def self.up  
	rename_column :users, :passwordExpiry, :passwordChanged
	execute "UPDATE users set passwordChanged=CURDATE()"		
  end
  
  def self.down    
	rename_column :users, :passwordChanged, :passwordExpiry
  end
  
end