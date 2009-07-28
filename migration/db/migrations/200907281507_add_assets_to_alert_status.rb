class AddAssetsToAlertStatus < ActiveRecord::Migration
  
  def self.up
    add_column(:alertstatus, :assets, :integer)
    
    execute ("update alertstatus set assets = 0")
    execute ("ALTER TABLE alertstatus ALTER assets SET NOT NULL")
  end
  
  def self.down
    remove_column(:alertstatus, :assets)
  end
end