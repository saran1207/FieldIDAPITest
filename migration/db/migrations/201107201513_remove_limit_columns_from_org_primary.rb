class RemoveLimitColumnsFromOrgPrimary < ActiveRecord::Migration

  def self.up
  	remove_column(:org_primary, :asset_limit)
  	remove_column(:org_primary, :diskspace_limit)
  	remove_column(:org_primary, :org_limit)
  	remove_column(:org_primary, :lite_user_limit)
  	remove_column(:org_primary, :readonly_user_limit)
  end

  def self.down
  end

end
