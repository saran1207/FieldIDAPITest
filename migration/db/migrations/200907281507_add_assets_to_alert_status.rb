class AddAssetsToAlertStatus < ActiveRecord::Migration
  
  def self.up
    add_column(:alertstatus, :assets, :integer)
    
    execute ("update alertstatus set assets = 0")
    change_column(:alertstatus, :assets, :integer, :null => false)
  end
  
  def self.down
    remove_column(:alertstatus, :assets)
  end
end