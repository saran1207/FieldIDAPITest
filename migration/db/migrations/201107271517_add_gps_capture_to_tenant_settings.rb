require "tenant_settings"

class AddGpsCaptureToTenantSettings < ActiveRecord::Migration

  def self.up  
	add_column(:tenant_settings, :gpsCapture, :boolean, :null=> false)
	TenantSettings.reset_column_information
    TenantSettings.update_all(:gpsCapture => false)
  end
  
  def self.down    
	remove_column(:tenant_settings, :gpsCapture)
  end
  
end