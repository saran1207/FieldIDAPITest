class GenerateMobileGuidsOnAssets < ActiveRecord::Migration
  def self.up
    execute("update assets set mobileguid = UUID() where mobileguid is null")
  end
  
  def self.down
  end
end