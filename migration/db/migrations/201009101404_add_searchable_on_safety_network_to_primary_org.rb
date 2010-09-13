require "primary_org"

class AddSearchableOnSafetyNetworkToPrimaryOrg < ActiveRecord::Migration

  def self.up
     add_column(:org_primary, :searchable_on_safety_network, :boolean, :null=> false)
     PrimaryOrg.reset_column_information
	 PrimaryOrg.update_all("searchable_on_safety_network = true");
  end

  def self.down
    remove_column(:org_primary, :searchable_on_safety_network)
  end

end
