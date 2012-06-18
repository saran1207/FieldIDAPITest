require "asset_type"
require "asset"

class AddLinkableFlagToAssetType < ActiveRecord::Migration
  def self.up
    add_column(:assettypes, :linkable, :boolean, :null=>false)

    AssetType.reset_column_information
    AssetType.update_all(:linkable => false)

    Asset.find_by_sql("select * from assets where id in (select distinct masterasset_id from subassets)").each do |asset|
      AssetType.update(asset.type_id, :linkable => true)
    end


  end

  def self.down
    remove_column(:assettypes, :linkable)
  end
end