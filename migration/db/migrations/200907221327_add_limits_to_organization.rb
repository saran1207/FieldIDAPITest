require "user"

INTEGER_MAX = (2**31) - 1

class AddLimitsToOrganization < ActiveRecord::Migration
  def self.up
    add_column(:organization, :diskspace_limit, :integer)
    add_column(:organization,:user_limit, :integer)
    
    execute "update organization set diskspace_limit = 262144000, user_limit = -1 where type = 'ORGANIZATION'"
    
    execute "update organization set user_limit = 3 where id = 15511541" # swos
    execute "update organization set user_limit = 5 where id = 15511544" # interstate
    execute "update organization set user_limit = 1 where id = 15511537" # domson
    execute "update organization set user_limit = 1 where id = 15511540" # hesco
    execute "update organization set user_limit = 1 where id = 15511538" # aoc
    execute "update organization set user_limit = 1 where id = 15511532" # flaherty
  end
  
  def self.down
    remove_column(:organization, :diskspace_limit)
    remove_column(:organization, :user_limit)
  end
end