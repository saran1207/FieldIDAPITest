class CleanNotifications < ActiveRecord::Migration
  def self.up
	  execute "delete from notificationsettings_eventtypes"
	  execute "delete from notificationsettings_addresses"
	  execute "delete from notificationsettings_assettypes"
	  execute "delete from notificationsettings"
    execute "delete from send_saved_item_schedules_emails"
    execute "delete from send_saved_item_schedules"
  end
  
  def self.down
    
  end
end
