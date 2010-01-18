require "customer_importing"

class AddFrenchJobSitesToReitmans < ActiveRecord::Migration
  def self.up
    reitmans = Tenant.find(:first, :conditions => { :name => "reitmans" })
    
    CustomerImporting.new().import('data/reitmans_job_sites_french.csv', reitmans);
  end
  
 
end