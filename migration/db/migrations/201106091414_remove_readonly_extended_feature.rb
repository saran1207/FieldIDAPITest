require "org_extendedfeature"
require "primary_org"

class RemoveReadonlyExtendedFeature < ActiveRecord::Migration
  
  def self.up
  	OrgExtendedfeature.find(:all, :conditions => {:feature => "ReadOnlyUser"}).each do |feature|
  	  org = PrimaryOrg.find(feature.org_id)
  	  org.readonly_user_limit = -1
  	  org.save
  	end
  	
  	OrgExtendedfeature.delete_all(:feature => "ReadOnlyUser")
  end

  def self.down
  
  	PrimaryOrg.find(:all, :conditions => "readonly_user_limit <> 0").each do |org|
  		OrgExtendedfeature.create(:org_id=> org.id, :feature=>'ReadOnlyUser')
  	end
  
  	PrimaryOrg.update_all("readonly_user_limit = 0");
  end

end