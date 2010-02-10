require "base_org"

class RenameExternalIds < ActiveRecord::Migration
  def self.up
	rename_column(:org_base, :external_id, :global_id)
    BaseOrg.reset_column_information
  end
  
  def self.down
  end
  
end