class AddSecondaryOrgLimit < ActiveRecord::Migration
  def self.up
    add_column(:org_primary, :org_limit, :integer)
    
    execute "update org_primary set org_limit = -1"
    execute "ALTER TABLE org_primary MODIFY org_limit BIGINT NOT NULL"
  end
  
  def self.down
    remove_column(:org_primary, :org_limit)
  end
end