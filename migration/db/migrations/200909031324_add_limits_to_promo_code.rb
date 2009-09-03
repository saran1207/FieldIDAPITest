class AddLimitsToPromoCode < ActiveRecord::Migration
  def self.up
    add_column(:promocodes, :assets_limit, :integer)
    add_column(:promocodes, :users_limit, :integer)
    add_column(:promocodes, :diskspace_limit, :integer)
    
    execute "UPDATE promocodes set assets_limit = 0, users_limit = 0, diskspace_limit = 0";
   
    change_column(:promocodes, :assets_limit, :integer, :null => false)
    change_column(:promocodes, :users_limit, :integer, :null => false)
    change_column(:promocodes, :diskspace_limit, :integer, :null => false)
    
  end
  
  def self.down
    remove_column(:promocodes, :assets_limit)
    remove_column(:promocodes, :users_limit)
    remove_column(:promocodes, :diskspace_limit)
  end
end