require "notification_settings"
class AddSmartFailureToNotificationSettings < ActiveRecord::Migration
  def self.up
    add_column(:notificationsettings, :smartfailure, :boolean)
    NotificationSettings.reset_column_information
    NotificationSettings.update_all(:smartfailure => false)
    change_column(:notificationsettings, :smartfailure, :boolean, :null => false)
  end
  
  def self.down
    remove_column(:notificationsettings, :smartfailure)
  end
end