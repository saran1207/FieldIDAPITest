class RenamePartnercenterToReadonlyuser < ActiveRecord::Migration
  def self.up   
	execute "update org_extendedfeatures set feature='ReadOnlyUser' where feature='PartnerCenter'"
  end
  
  def self.down
    execute "update org_extendedfeatures set feature='PartnerCenter' where feature='ReadOnlyUser'"
  end
end