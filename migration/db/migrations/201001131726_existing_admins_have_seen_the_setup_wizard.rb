require "seen_it_storage_item"
require "user"
class ExistingAdminsHaveSeenTheSetupWizard < ActiveRecord::Migration
  def self.up
    User.transaction do
      admin_users = User.find(:all, :conditions => { :admin => true })
      admin_users.each do |admin_user|
        seenIt = SeenItStorageItem.create(add_time_hash_to(:userid => admin_user.id))
        ItemSeen.create(:seenitstorageitem_id => seenIt.id, :element => 'SetupWizard')
      end
    end
  end
end