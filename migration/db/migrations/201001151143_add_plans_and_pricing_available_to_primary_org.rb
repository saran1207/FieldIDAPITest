require "primary_org"
class AddPlansAndPricingAvailableToPrimaryOrg < ActiveRecord::Migration
  def self.up
    add_column(:org_primary, :plansandpricingavailable, :boolean)
    PrimaryOrg.reset_column_information
    PrimaryOrg.update_all(:plansandpricingavailable => false)
    change_column(:org_primary, :plansandpricingavailable, :boolean, :null => false)
  end
  
   def self.down
    remove_column(:org_primary, :plansandpricingavailable)
    PrimaryOrg.reset_column_information
  end
end