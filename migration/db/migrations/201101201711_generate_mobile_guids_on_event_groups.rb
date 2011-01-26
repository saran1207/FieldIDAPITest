class GenerateMobileGuidsOnEventGroups < ActiveRecord::Migration
  def self.up
    execute("update eventgroups set mobileguid = UUID() where mobileguid is null")
  end
  
  def self.down
  end
  
end