class AddSyncDurationToOfflineProfile < ActiveRecord::Migration
  def self.up
    add_column(:offline_profiles, :sync_duration, :string)
    execute("UPDATE offline_profiles SET sync_duration = 'YEAR'")
    change_column(:offline_profiles, :sync_duration, :string, :null => false)
  end

  def self.down
    remove_column(:offline_profiles, :sync_duration)
  end
end