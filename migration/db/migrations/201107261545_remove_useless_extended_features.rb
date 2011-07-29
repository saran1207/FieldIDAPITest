class RemoveUselessExtendedFeatures < ActiveRecord::Migration

  def self.up
  	execute "delete from org_extendedfeatures where feature in ('CustomCert', 'DedicatedProgramManager', 'AllowIntegration', 'UnlimitedLinkedAssets')"
  end

  def self.down
  end

end
