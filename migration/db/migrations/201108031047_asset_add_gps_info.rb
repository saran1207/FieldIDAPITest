require 'column_mapping'

class AssetAddGpsInfo < ActiveRecord::Migration

  def self.up1
  	add_column :assets, :latitude, :decimal, :precision=>15, :scale=>10
  	add_column :assets, :longitude, :decimal, :precision=>15, :scale=>10
  end

  def self.down
  	remove_column :assets, :latitude
  	remove_column :assets, :longitude
  end

end