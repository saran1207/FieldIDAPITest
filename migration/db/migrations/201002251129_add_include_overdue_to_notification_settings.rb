require "notification_settings"
class AddIncludeOverdueToNotificationSettings < ActiveRecord::Migration
  def self.up
    add_column(:notificationsettings, :includeoverdue, :boolean)
    add_column(:notificationsettings, :includeupcoming, :boolean)
    NotificationSettings.reset_column_information
    NotificationSettings.update_all(:includeoverdue => false, :includeupcoming => true)
    change_column(:notificationsettings, :includeoverdue, :boolean, :null => false)
    change_column(:notificationsettings, :includeupcoming, :boolean, :null => false)
  end
  
  def self.down
    remove_column(:notificationsettings, :includeoverdue)
  end
end