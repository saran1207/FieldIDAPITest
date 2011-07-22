class TenantSettingsSecurityPolicy < ActiveRecord::Migration

  def self.up  
	add_column(:tenant_settings, :maxAttempts, :integer, :null => false, :default => 5)  
	add_column(:tenant_settings, :lockoutDuration, :integer, :null => false, :default=> 20)  
	add_column(:tenant_settings, :minLength, :integer, :null => false, :default=> 6)  
	add_column(:tenant_settings, :minNumbers, :integer, :null => false, :default=> 0)  
	add_column(:tenant_settings, :minSymbols, :integer, :null => false, :default=> 0)  
	add_column(:tenant_settings, :minCapitals, :integer, :null => false, :default=> 0)  
	add_column(:tenant_settings, :expiryDays, :integer, :null => false, :default=> 0)  
	add_column(:tenant_settings, :uniqueness, :integer, :null => false, :default=> 0)  
  end
  
  def self.down    
	remove_column(:tenant_settings, :maxAttempts)  
	remove_column(:tenant_settings, :lockoutDuration)  
	remove_column(:tenant_settings, :minLength)  
	remove_column(:tenant_settings, :minNumbers)  
	remove_column(:tenant_settings, :minSymbols)  
	remove_column(:tenant_settings, :minCapitals)  
	remove_column(:tenant_settings, :expiryDays)  
	remove_column(:tenant_settings, :uniqueness)  
  end
  
end