require "base_org"
require "customer_org"
require "division_org"
require "asset"
require "master_event"

class RemoveUnusedMazzellaCustomers < ActiveRecord::Migration
    
    def self.up
      tenantId = 15511608
      
      BaseOrg.transaction do
        BaseOrg.find_each(:conditions => ["customer_id IS NOT NULL AND division_id IS NULL AND tenant_id = ?", tenantId]) do |org|
          
          divisions = DivisionOrg.count(:conditions => {:parent_id => org.id})
          if divisions > 0 then
            next
          end
          
          assets = Asset.count(:conditions => {:owner_id => org.id})
          if assets == 0 then
            events = MasterEvent.count(:conditions => {:owner_id => org.id})
            if events == 0 then
              puts "Removing: " + org.name
              CustomerOrg.destroy(org.id)
              org.destroy()
            end
          end
        end
      end
    end

    def self.down
    end
    
end
