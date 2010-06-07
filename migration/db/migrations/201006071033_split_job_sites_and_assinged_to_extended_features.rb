require "org_extendedfeature"
class SplitJobSitesAndAssingedToExtendedFeatures < ActiveRecord::Migration
  def self.up
    PrimaryOrg.transaction do
      OrgExtendedfeature.find(:all, :conditions => { :feature => "JobSites" }).each do |feature|
        OrgExtendedfeature.create(:feature => "AssignedTo", :org_id => feature.org_id)
      end
    end
  end
  
  def self.down
    OrgExtendedfeature.delete_all(:all, :conditions => { :feature => "AssignedTo" })
  end
end