require "product"
require "asset_extension_value"
require 'info_option'

class MoveCgliftsAssetextensionvalueToInfofields < ActiveRecord::Migration

  def self.up
    AssetExtensionValue.find(:all).each do |extension|
      assetToUpdate = Product.first(:id=> extension.r_productserial)
      infoFieldToUpdate = InfoField.first(:r_productinfo => assetToUpdate.type_id)
     
      InfoOption.create(:name=> extension.extensionvalue, :r_infofield => infoFieldToUpdate.id, :staticdata=> '0', :weight=>'0' )

    end
  end
end
  