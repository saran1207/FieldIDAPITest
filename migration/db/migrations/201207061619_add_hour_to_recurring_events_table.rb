
class AddHourToRecurringEventsTable < ActiveRecord::Migration

  def self.up
    add_column(:recurring_asset_type_events, :hour, :integer, :null=>true, :default=>0)
    rename_column(:recurring_asset_type_events, :recurrence, :recurrence_type)
  end

  def self.down
      remove_column(:recurring_asset_type_events, :hour)
  end

end