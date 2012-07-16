require 'column_mapping'

class AddDayToRecurrence < ActiveRecord::Migration

  def self.up
    add_column(:recurring_asset_type_events, :day, :timestamp, :null=>true)
  end

  def self.down
    remove_column(:recurring_asset_type_events, :day)
  end

end