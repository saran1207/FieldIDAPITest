class RemoveDefaultvendorcontextColumnFromOrgPrimary < ActiveRecord::Migration

  def self.up
    remove_column "org_primary", "defaultvendorcontext"
  end

  def self.down
    add_column "org_primary", "defaultvendorcontext"
  end

end