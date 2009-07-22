require "user"

INTEGER_MAX = (2**31) - 1

class AddLimitsToOrganization < ActiveRecord::Migration
  def self.up
    add_column(:organization, :diskspace_limit, :integer)
    add_column(:organization,:user_limit, :integer)
    
    execute "update organization set diskspace_limit = 0, user_limit = 0 where type = 'ORGANIZATION'"
  end
  
  def self.down
    remove_column(:organization, :diskspace_limit)
    remove_column(:organization, :user_limit)
  end
end