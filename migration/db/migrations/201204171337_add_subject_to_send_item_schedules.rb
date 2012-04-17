require 'send_saved_item_schedule'

class AddSubjectToSendItemSchedules < ActiveRecord::Migration

  def self.up
    add_column(:send_saved_item_schedules, :subject, :string, { :limit => 255, :null => false } )
    SendSavedItemSchedule.reset_column_information
    SendSavedItemSchedule.update_all(:subject => "FieldID: Saved Item")
  end

  def self.down
    remove_column(:send_saved_item_schedules, :subject)
  end

end