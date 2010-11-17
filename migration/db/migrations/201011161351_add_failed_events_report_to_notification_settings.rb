require "notification_settings"
class AddFailedEventsReportToNotificationSettings < ActiveRecord::Migration
  def self.up
    add_column(:notificationsettings, :includefailed, :boolean)
    NotificationSettings.reset_column_information
    NotificationSettings.update_all(:includefailed => false)
    change_column(:notificationsettings, :includefailed, :boolean, :null => false)
  end
  
  def self.down
    remove_column(:notificationsettings, :includefailed)
  end
end