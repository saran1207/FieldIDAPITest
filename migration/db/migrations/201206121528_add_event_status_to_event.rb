require "event"

class AddEventStatusToEvent < ActiveRecord::Migration

  def self.up
    add_column(:events, :eventstatus_id, :integer)
    Event.reset_column_information
    Event.update_all("eventstatus_id = null")
  end

  def self.down
    remove_column(:events, :eventstatus_id)
  end

end