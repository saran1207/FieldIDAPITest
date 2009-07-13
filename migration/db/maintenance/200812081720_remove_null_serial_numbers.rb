require "product"
class RemoveNullSerialNumbers < ActiveRecord::Migration
  def self.up
    Product.transaction do
	
      execute " delete from productserial_infooption where r_productserial IN ( 15691372, 15691373, 15691374, 15691392, 15691393, 15691394, 15691371, 15691395 ) "
      execute " delete from products where id IN ( 15691372, 15691373, 15691374, 15691392, 15691393, 15691394, 15691371, 15691395 ) "
    end
  end
end
