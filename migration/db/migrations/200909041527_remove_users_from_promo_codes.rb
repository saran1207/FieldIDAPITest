class RemoveUsersFromPromoCodes < ActiveRecord::Migration
  def self.up
    remove_column(:promocodes, :user_limit)
  end
  
  def self.down
     add_column(:promocodes, :user_limit, :integer)
     execute "UPDATE promocodes set user_limit = 0";   
  end
end