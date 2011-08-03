require 'column_mapping'

class MasterEventAddGpsInfo < ActiveRecord::Migration

  def self.up
  	remove_column :events, :latitude
  	remove_column :events, :longitude
  	add_column :masterevents, :latitude, :decimal, :precision=>15, :scale=>10
  	add_column :masterevents, :longitude, :decimal, :precision=>15, :scale=>10
  end

  def self.down
  
  end

end