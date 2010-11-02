require "asset"
require "asset_extension_value"
require 'info_option'
require 'asset_type'
require 'asset_infooption'

class MoveCgliftsAssetextensionvalueToInfofields < ActiveRecord::Migration

  def self.up
    AssetExtensionValue.find(:all).each do |extension|
      assetToUpdate = Asset.find_by_id(extension.r_productserial)
      infoFieldToUpdate = InfoField.find(:first, :conditions => {:r_productinfo => assetToUpdate.type_id, :name=>'Part Number'})

      infoOptionToUpdate = InfoOption.create(:name=> extension.extensionvalue, :r_infofield => infoFieldToUpdate.uniqueid, :staticdata=> '0', :weight=>'0' )

      AssetInfooption.create(:r_productserial => extension.r_productserial, :r_infooption => infoOptionToUpdate.uniqueid )
    end
  end
end