class RemoveJobSitesFromExtendedFeatures < ActiveRecord::Migration
  
	def self.up
    
    execute("delete from org_extendedfeatures where feature = 'JobSites'")

  end
	
	def self.down
  end
end