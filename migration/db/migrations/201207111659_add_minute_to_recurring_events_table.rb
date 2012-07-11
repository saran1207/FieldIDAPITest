
class AddMinuteToRecurringEventsTable < ActiveRecord::Migration

  def self.up
    add_column(:recurring_asset_type_events, :minute, :integer, :null=>true, :default=>0)
  end

  def self.down
      remove_column(:recurring_asset_type_events, :minute)
  end

end