require 'notification_settings'

class AddEventtypegroupColumnToNotification < ActiveRecord::Migration
  def self.up
    add_column :notificationsettings, :eventtypegroup, :integer
  end

  def self.down
    remove_column :notificationsettings, :eventtypegroup
  end
end