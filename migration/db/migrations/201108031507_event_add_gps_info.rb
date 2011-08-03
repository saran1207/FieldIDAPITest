require 'column_mapping'

class EventAddGpsInfo < ActiveRecord::Migration

  def self.up
  	add_column :events, :latitude, :decimal, :precision=>15, :scale=>10
  	add_column :events, :longitude, :decimal, :precision=>15, :scale=>10
  end

  def self.down
  	remove_column :events, :latitude
  	remove_column :events, :longitude
  end

end