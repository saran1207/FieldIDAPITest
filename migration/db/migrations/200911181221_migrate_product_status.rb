require "inspection"

class MigrateProductStatus < ActiveRecord::Migration
  def self.up
  	Inspection.find_each do |inspection|
  		inspection.productstatus_id = inspection.product.productstatus_uniqueid
  		inspection.save
  	end
  end
  
  def self.down
  	
  end
end