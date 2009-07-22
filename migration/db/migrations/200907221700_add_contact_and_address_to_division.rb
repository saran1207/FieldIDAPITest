require "division"
class AddContactAndAddressToDivision < ActiveRecord::Migration
  def self.up
    add_column(:divisions, :contactname, :string)
    add_column(:divisions, :contactemail, :string)
    add_column(:divisions, :addressinfo_id, :int)
    
    foreign_key(:divisions, :addressinfo_id, :addressinfo, :id)
  end
  
  def self.down
    remove_column(:divisions, :contactname)
    remove_column(:divisions, :contactemail)
    remove_column(:divisions, :address_id)
  end
end 