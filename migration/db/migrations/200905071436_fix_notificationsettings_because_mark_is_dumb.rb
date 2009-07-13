class FixNotificationsettingsBecauseMarkIsDumb < ActiveRecord::Migration
  
  def self.up

    remove_column(:notificationsettings, :notifyDay)
    remove_column(:notificationsettings, :notifyWeekly)

    add_column(:notificationsettings,  :notifyday, :integer, :null => false)
    add_column(:notificationsettings,  :notifyweekly, :boolean, :null => false)
    
  end
  
  def self.down
    
  end
  
end