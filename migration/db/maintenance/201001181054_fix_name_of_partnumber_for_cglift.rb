require "product_serial_extension"
class FixNameOfPartnumberForCglift < ActiveRecord::Migration
  def self.up
    ProductSerialExtension.update_all(:extensionlabel => "Part Number")
  end
end