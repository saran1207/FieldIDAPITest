require "product"
require "asset_extension_value"
require 'info_option'

class MoveCgliftsAssetextensionvalueToInfofields < ActiveRecord::Migration

  def self.up
    AssetExtensionValue.find(:all).each do |extension|
      assetToUpdate = Product.where(:id => extension.r_productserial).first
      infoFieldToUpdate = InfoField.where(:r_productinfo => assetToUpdate.type_id).first
     
      InfoOption.create(:name=> extension.extensionvalue, :r_infofield => infoFieldToUpdate.id, :staticdata=> '0', :weight=>'0' )

    end
  end
end
  