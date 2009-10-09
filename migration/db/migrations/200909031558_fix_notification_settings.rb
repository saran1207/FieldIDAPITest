require "notification_settings_owner"

class FixNotificationSettings < ActiveRecord::Migration
  
	def self.up
    
    remove_column(:notificationsettings, :usingjobsite)
    add_column(:notificationsettings, :owner_id, :integer)
    add_foreign_key(:notificationsettings, :org_base,  :source_column => :owner_id, :foreign_column => :id, :name => "fk_notificationsettings_owner")
    NotificationSettingsOwner.reset_column_information
    
    NotificationSettingsOwner.find_each do |noteOwner|
      noteSetting = noteOwner.notificationsettings
      noteSetting.owner_id = noteOwner.owner_id
      noteSetting.save
    end
    
    drop_table(:notificationsettings_owner)
  end
	
	def self.down
  end
end