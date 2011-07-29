require "primary_org"
require "asset_type"

class AddIdentifierLabelToPrimaryOrg < ActiveRecord::Migration

  def self.up
    add_column :org_primary, :identifierLabel, :string, :limit => 255, :null => false
    add_column :assettypes, :identifierOverridden, :boolean, :null => false
    PrimaryOrg.reset_column_information
    PrimaryOrg.update_all(:identifierLabel => 'Serial Number')
    AssetType.reset_column_information
    AssetType.update_all(:identifierOverridden => false)
    rename_column :org_primary, :serialnumberformat, :identifierFormat
  end

  def self.down
    remove_column :org_primary, :identifierLabel
    remove_column :assettypes, :identifierOverridden

    rename_column :org_primary, :identifierFormat, :serialnumberformat
  end

end