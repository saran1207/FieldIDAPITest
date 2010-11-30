require "user"

class AddEmployeeFlagToUser < ActiveRecord::Migration
  def self.up
    add_column(:users, :employee, :boolean)
    User.reset_column_information
    User.update_all(:employee => false)
    change_column(:users, :employee, :boolean, :null => false)
  end
  
  def self.down
    remove_column(:users, :employee)
  end
end