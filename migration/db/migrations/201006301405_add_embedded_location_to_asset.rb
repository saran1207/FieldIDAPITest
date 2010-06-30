require 'product'
class AddEmbeddedLocationToAsset < ActiveRecord::Migration
  def self.up
    add_column(:products, :predefinedlocation_id, :integer)
    add_foreign_key(:products, :predefinedlocations,  :source_column => :predefinedlocation_id, :foreign_column => :id)
  end
  
  def self.down
    remove_column(:products, :predefinedlocation_id)
  end
end