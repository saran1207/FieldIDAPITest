require "base_org"

class AddStateToOrgs < ActiveRecord::Migration

  def self.up
     add_column(:org_base, :state, :string, :null=> false)
     BaseOrg.reset_column_information
	 BaseOrg.update_all("state = 'ACTIVE'");
  end

  def self.down
    remove_column(:org_base, :state)
  end

end
