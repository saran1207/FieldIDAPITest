require 'org_extendedfeature'

class AddProoftestExtendedFeatureToAllTenants < ActiveRecord::Migration
  def self.up
    PrimaryOrg.find(:all).each do |org|
      newFeature = OrgExtendedfeature.create(:org_id=> org.id, :feature=>'ProofTestIntegration')
    end
  end

  def self.down
   OrgExtendedfeature.delete_all(:all, :conditions => { :feature => "ProofTestIntegration" })
  end
end
