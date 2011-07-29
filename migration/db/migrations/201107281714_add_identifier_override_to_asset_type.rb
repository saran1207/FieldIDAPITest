class AddIdentifierOverrideToAssetType < ActiveRecord::Migration

  def self.up
    add_column :assettypes, :identifierFormat, :string, :limit => 255
    add_column :assettypes, :identifierLabel, :string, :limit => 255
  end

  def self.down
    remove_column :assettypes, :identifierFormat
    remove_column :assettypes, :identifierLabel
  end

end