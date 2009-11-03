require "user"
class FixAdminUsersPermission < ActiveRecord::Migration
  def self.up
    User.update_all("permissions = 2147483647", :admin => true)
  end
end