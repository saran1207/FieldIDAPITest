
class AddRecurringEventToEventsTable < ActiveRecord::Migration

  def self.up
    add_column(:masterevents, :recurring_event_id, :integer, :null=>true)
    add_foreign_key(:masterevents, :recurring_asset_type_events, :source_column => :recurring_event_id, :foreign_column => :id)
  end

  def self.down
      remove_column(:masterevents, :recurring_event_id)
  end

end