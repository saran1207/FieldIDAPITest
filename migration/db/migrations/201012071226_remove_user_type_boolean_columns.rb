require "user"

class RemoveUserTypeBooleanColumns < ActiveRecord::Migration

  def self.up
       remove_column(:users, :system)
       remove_column(:users, :admin)
       remove_column(:users, :employee)
  end

end