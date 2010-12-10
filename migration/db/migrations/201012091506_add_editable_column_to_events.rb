require 'event'

class AddEditableColumnToEvents < ActiveRecord::Migration

  def self.up
    add_column(:events, :editable, :boolean, :null => false, :default => true)
    execute("update events,eventtypes set events.editable = false where events.formversion <> eventtypes.formversion and events.type_id = eventtypes.id")
    remove_column(:events, :formversion)
  end

  def self.down
    remove_column(:events, :editable)
    add_column(:events, :formversion, :integer)
    Event.update_all(:formversion => 1)
    change_column(:events, :formversion, :integer, :null => false)
  end

end