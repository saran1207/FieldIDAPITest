class RemoveAssetExtensionAndAssetExtensionValueTables < ActiveRecord::Migration

  def self.up
    drop_table(:assetextensionvalue)
    drop_table(:assetextension)
  end

end