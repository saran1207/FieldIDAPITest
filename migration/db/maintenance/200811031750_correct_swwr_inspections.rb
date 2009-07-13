require "organization"
require "inspection"
require "user"

class CorrectSwwrInspections < ActiveRecord::Migration
  def self.up
    Inspection.transaction do
      tenantName = "swwr"
      badUserName = "n4systems"
      
      tenant = Organization.find(:first, :conditions => ["type = :type and name = :tenantName", {:type => "ORGANIZATION", :tenantName => tenantName}])
      puts "Tenant: " + tenant.displayString
      badUser = User.find(:first, :conditions => ["r_tenant = :tenantId and userid = :userName", {:tenantId => tenant.id, :userName => badUserName}])
      puts "User: " + badUser.displayString
      
      inspections = Inspection.find(:all, :conditions => ["r_tenant = :tenantId and inspector_uniqueid = :moduser", {:tenantId => tenant.id, :moduser => badUser.uniqueid}])
      
      puts "Found " + inspections.size.to_s + " inspections"
      for inspection in inspections do
        puts "Found: " + inspection.displayString + ".  Setting inspector to " + inspection.product.identifiedBy.displayString
        inspection.inspector = inspection.product.identifiedBy
        inspection.save
      end
    end
  end
  def self.down
    raise ActiveRecord::IrreversibleMigration
  end
end