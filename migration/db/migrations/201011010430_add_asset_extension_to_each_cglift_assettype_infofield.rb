require "asset_type"
require "info_field"

class AddAssetExtensionToEachCgliftAssettypeInfofield < ActiveRecord::Migration

  def self.up
    AssetType.all(:conditions => {:tenant_id => '132385', :state=> 'active'}).each do |type|
      InfoField.create(:name => "Part Number",
        :r_productinfo => type.id,
        :required => '0',
        :usingunitofmeasure => '0',
        :weight => InfoField.all(:conditions =>{ :r_productinfo => type.id}).size + 1,
        :fieldtype => 'textfield',
        :retired => '0')
    end
  end

end

