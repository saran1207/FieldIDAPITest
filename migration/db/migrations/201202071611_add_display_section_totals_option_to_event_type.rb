require 'event_type'

class AddDisplaySectionTotalsOptionToEventType < ActiveRecord::Migration

  def self.up
    add_column(:eventtypes, :display_section_totals, :boolean, :null => false)
    EventType.update_all({ :display_section_totals => false })
  end

  def self.down
    remove_column :eventtypes, :display_section_totals
  end
  
end