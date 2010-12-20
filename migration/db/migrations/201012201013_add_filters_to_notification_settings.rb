require "notification_settings"
class AddFiltersToNotificationSettings < ActiveRecord::Migration
  def self.up
    add_column(:notificationsettings, :assetStatus, :integer)
    add_column(:notificationsettings, :assetTypeGroup, :integer)
    NotificationSettings.reset_column_information
  end
  
  def self.down
    remove_column(:notificationsettings, :assetStatus)
    remove_column(:notificationsettings, :assetTypeGroup)
  end
end