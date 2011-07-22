class RemoveCountsTowardsLimitFromAsset < ActiveRecord::Migration

  def self.up
  	remove_column "assets", "countstowardslimit"
  end

  def self.down
  end

end
