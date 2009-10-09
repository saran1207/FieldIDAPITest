require "tenant"
require "user"
class MigrateUserOrgs < ActiveRecord::Migration
  
  def self.up
    Tenant.transaction do
      
      drop_foreign_key(:users, :organization, :source_column => :r_organization, :foreign_column => :id, :name => "fk_the_users_organization")
      rename_column(:users, :r_organization, :organization_id)
      add_foreign_key(:users, :org_base, :source_column => :organization_id, :foreign_column => :id)
      User.reset_column_information
    end

  end
  
  def self.down
  end
  
end