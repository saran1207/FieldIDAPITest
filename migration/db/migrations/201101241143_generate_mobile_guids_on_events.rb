class GenerateMobileGuidsOnEvents < ActiveRecord::Migration
  def self.up
    execute("update events set mobileguid = UUID() where mobileguid is null")
    add_index(:events, :mobileguid, :unique => true)
  end
  
  def self.down
  end
end