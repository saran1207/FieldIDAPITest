require "tenant"

class NotificationSettings < ActiveRecord::Base
  set_table_name :notificationsettings

  belongs_to  :tenant,      :foreign_key => 'tenant_id',       :class_name => 'Tenant'
end
