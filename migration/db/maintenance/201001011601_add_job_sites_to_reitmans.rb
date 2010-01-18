require "customer_importing"

class AddJobSitesToReitmans < ActiveRecord::Migration
  def self.up
    reitmans = Tenant.find(:first, :conditions => { :name => "reitmans" })
    
    CustomerImporting.new().import('data/reitmans_job_sites.csv', reitmans);
  end
  
 
end