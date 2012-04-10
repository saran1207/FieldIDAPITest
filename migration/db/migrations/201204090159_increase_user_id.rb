class IncreaseUserId < ActiveRecord::Migration

  def self.up
    change_column(:users, :userid, :string, {:limit => 255})
  end

  def self.down
    change_column(:users, :userid, :string, {:limit => 15})
  end
  
end