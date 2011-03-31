class CorrectSortColumnOnOldSavedReports < ActiveRecord::Migration

  def self.up
    execute "update savedreports set sortcolumn = 'advancedLocation.freeformLocation' where sortcolumn='location'"
    execute "update savedreports set sortcolumn = 'assetStatus.name' where sortcolumn='asset.assetStatus.name'"
  end

  def self.down
    execute "update savedreports set sortcolumn = 'location' where sortcolumn='advancedLocation.freeformLocation'"
    execute "update savedreports set sortcolumn = 'asset.assetStatus.name' where sortcolumn='assetStatus.name'"
  end

end