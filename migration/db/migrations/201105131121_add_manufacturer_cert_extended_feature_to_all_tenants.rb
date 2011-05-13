require 'org_extendedfeature'

class AddManufacturerCertExtendedFeatureToAllTenants < ActiveRecord::Migration
  def self.up
    PrimaryOrg.find(:all).each do |org|
      newFeature = OrgExtendedfeature.create(:org_id=> org.id, :feature=>'ManufacturerCertificate')
    end
  end

  def self.down
   OrgExtendedfeature.delete_all(:all, :conditions => { :feature => "ManufacturerCertificate" })
  end
end
