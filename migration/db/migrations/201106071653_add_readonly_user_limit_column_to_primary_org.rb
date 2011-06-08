require "primary_org"

class AddReadonlyUserLimitColumnToPrimaryOrg < ActiveRecord::Migration
  
  def self.up
     add_column(:org_primary, :readonly_user_limit, :integer, { :default => 0 })
  end

  def self.down
    remove_column(:org_primary, :readonly_user_limit)
  end

end