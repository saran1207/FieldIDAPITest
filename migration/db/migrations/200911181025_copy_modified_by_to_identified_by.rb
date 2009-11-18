require "product"

class CopyModifiedByToIdentifiedBy < ActiveRecord::Migration
  def self.up
  
  	total = 0
  	Product.find_each(:conditions => 'identifiedby_uniqueid IS NULL AND modifiedby IS NOT NULL') do |product|
  		product.identifiedby_uniqueid = product.modifiedby
  		product.save
  		total += 1
  	end
  	puts "Updated " + total.to_s + " products"
  end
  
  def self.down
  end
end