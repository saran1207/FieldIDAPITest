require "base_org"

class AddExternalIdToOrgs < ActiveRecord::Migration
  def self.up
    add_column(:org_base, :external_id, :string, :limit => 36)
    add_index(:org_base, :external_id, :unique => true)
    BaseOrg.reset_column_information
  end
  
  def self.down
    remove_column(:org_base, :external_id)
    BaseOrg.reset_column_infomation
  end
end