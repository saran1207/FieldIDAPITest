class AddAssetLimit < ActiveRecord::Migration
  def self.up
    add_column(:organization, :asset_limit, :integer)
    
    execute "update organization set asset_limit = -1 where type = 'ORGANIZATION'"
  end
  
  def self.down
    remove_column(:organization, :asset_limit)
  end
end