class AddMobileIdToAssetAttachments < ActiveRecord::Migration

  def self.up
    add_column(:assetattachments, :mobileid, :string)
    add_index(:assetattachments, :mobileid, :name => "assetattachments_mobileid_unique", :unique => true)
    execute("UPDATE assetattachments SET mobileid = uuid()");
    change_column(:assetattachments, :mobileid, :string, :null => false)
  end

  def self.down
    remove_column(:assetattachments, :mobileid)
  end
  
end