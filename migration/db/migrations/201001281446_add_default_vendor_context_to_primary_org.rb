require "primary_org"

class AddDefaultVendorContextToPrimaryOrg < ActiveRecord::Migration
  def self.up
    add_column(:org_primary, :defaultvendorcontext, :integer)
    PrimaryOrg.reset_column_information
  end
  
  def self.down
    remove_column(:org_primary, :defaultvendorcontext)
    PrimaryOrg.reset_column_infomation
  end
end