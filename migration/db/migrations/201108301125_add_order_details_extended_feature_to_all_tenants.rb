require 'org_extendedfeature'

class AddOrderDetailsExtendedFeatureToAllTenants < ActiveRecord::Migration
  def self.up
    PrimaryOrg.find(:all).each do |org|
      newFeature = OrgExtendedfeature.create(:org_id=> org.id, :feature=>'OrderDetails')
    end
  end

  def self.down
   OrgExtendedfeature.delete_all(:all, :conditions => { :feature => "OrderDetails" })
  end
end
