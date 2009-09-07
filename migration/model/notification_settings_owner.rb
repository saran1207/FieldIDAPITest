require "notification_settings"

class NotificationSettingsOwner < ActiveRecord::Base
  set_table_name :notificationsettings_owner

  belongs_to  :notificationsettings,      :foreign_key => 'notificationsettings_id',       :class_name => 'NotificationSettings'
  
  def tenant_id
    notificationsettings.tenant_id
  end
  
end
