class RemoveUserLimitColumnFromOrgPrimary < ActiveRecord::Migration

  def self.up
  	remove_column(:org_primary, :user_limit)
  end

  def self.down
  end

end
