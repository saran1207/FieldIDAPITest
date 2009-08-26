require "user"
require "primary_org"

class AddExternalIdToPrimaryOrgAndAdminUser < ActiveRecord::Migration
  def self.up
    add_column(:org_primary, :externalid, :integer)
    add_column(:users, :externalid, :integer)
    User.reset_column_information
    PrimaryOrg.reset_column_information
  end
  
  def self.down
    remove_column(:org_primary, :externalid)
    remove_column(:users, :externalid)
    User.reset_column_information
    PrimaryOrg.reset_column_information
  end
end