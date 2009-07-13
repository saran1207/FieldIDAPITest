class AddUrlToTenant < ActiveRecord::Migration
  def self.up
    add_column(:organization, :website, :string, :limit => 2056)  
  end
  
  def self.down
    remove_column(:organization, :website)
  end
end