require "notification_settings"
class AddNotificationOptionToNotificationSettings < ActiveRecord::Migration
  def self.up
    add_column(:notificationsettings, :sendBlankReport, :boolean)
    NotificationSettings.reset_column_information
    NotificationSettings.update_all(:sendBlankReport => false)
    change_column(:notificationsettings, :sendBlankReport, :boolean, :null => false)
  end
  
  def self.down
    remove_column(:notificationsettings, :sendBlankReport)
  end
end