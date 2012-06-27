
class AddAssetAuditTable < ActiveRecord::Migration

  def self.up
      create_table "assetaudit", :primary_key => "id", :force => true do |t|
        create_entity_with_tenant_fields_on(t)
        t.timestamp     :identified,                                :null => true
        t.string        :owner,                                     :null => true
        t.string        :location,                                  :null => true
        t.string        :purchase_order,                            :null => true
        t.string        :asset_status,                              :null => true
        t.string        :comments,                                  :null => true
        t.string        :published,                                 :null => true
        t.string        :user_name,                                 :null => false
      end

     create_table "assetaudit_asset", :primary_key => "id", :force => true do |t|
        t.integer :asset_id, :null => false
        t.integer :assetaudit_id, :null => false
      end

      add_foreign_key(:assetaudit_asset, :assets, :source_column => :asset_id, :foreign_column => :id, :name => "fk_assetaudit_asset")
      add_foreign_key(:assetaudit_asset, :assetaudit, :source_column => :audit_id, :foreign_column => :id, :name => "fk_assetaudit_audit")
  end

  def self.down
    drop_table :assetaudit
    drop_table :assetaudit_asset
  end

end