require 'send_saved_item_schedule'

class AddFormatToSavedItemSchedules < ActiveRecord::Migration

  def self.up
    add_column(:send_saved_item_schedules, :format, :string, :null => false)
    SendSavedItemSchedule.reset_column_information
    SendSavedItemSchedule.update_all(:format => "HTML")
  end

  def self.down
    remove_column(:send_saved_item_schedules, :format)
  end

end