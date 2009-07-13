class AddNotificationsettingsFk < ActiveRecord::Migration
  
  def self.up

    
    foreign_key(:notificationsettings_addresses, :notificationsettings_id, :notificationsettings, :id)

  end
  
  def self.down
    
  end
  
end