class CleanNotifications < ActiveRecord::Migration
  def self.up
	  execute "delete from notificationsettings_owner"
	  execute "delete from notificationsettings_inspectiontypes"
	  execute "delete from notificationsettings_addresses"
	  execute "delete from notificationsettings"
  end
  
  def self.down
    
  end
end