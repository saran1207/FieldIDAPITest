class RemoveMultiLocationFromExtendedFeatures < ActiveRecord::Migration

  def self.up
  	execute "delete from org_extendedfeatures where feature = 'MultiLocation'"
  end

  def self.down
  end

end
