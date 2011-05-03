class CleanNotifications < ActiveRecord::Migration
  def self.up
	  execute "delete from notificationsettings_eventtypes"
	  execute "delete from notificationsettings_addresses"
	  execute "delete from notificationsettings_assettypes"
	  execute "delete from notificationsettings"
  end
  
  def self.down
    
  end
end
