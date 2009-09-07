require "customer"
require "division"
require "tenant"
require "base_org"
require "primary_org"
require "secondary_org"
require "customer_org"
require "division_org"
require "project"
require "product_type_schedule"
require "order"
require "notification_settings_owner"
require "inspectionmaster"
require "inspection_schedule"
require "inspection_book"
require "product"
require "add_product_history"
require "user"

class MigrateOwners < ActiveRecord::Migration
	def self.up
    Tenant.transaction do
    
      tables = [
        Project,
        ProductTypeSchedule,
        Order,
        NotificationSettingsOwner,
        Inspection,
        InspectionSchedule,
        InspectionBook,
        Product,
        AddProductHistory,
        User
      ]
   
      tabc = 1
      tables.each do |table|
        puts "Table #{table}: #{tabc} / #{tables.length}" 
        
        recc = 1
        rectot = table.count
        table.find_each do |rec|
          if (recc % 1000 == 0)
            puts "\tRecord #{table}: #{recc} / #{rectot}"
          end
          
          update_rec_owner(rec)
          recc += 1
        end
        
        tabc += 1
        
      end
      
    end
  end
	
	def self.down
  end

  def self.update_rec_owner(rec)
    
    def rec.method_missing(property, *args, &block)
      if (has_attribute?(property))
        super
      else
        return nil
      end
    end

    if (!rec.jobsite_id.nil?)
      # jobsites are migrated in a later migration
      return
    end
    
    tenantId = rec.tenant_id
    organizationId = rec.organization_id
    customerId = rec.customer_id
    divisionId = rec.division_id
    
    rec.owner_id = resolve_base_owner(tenantId, organizationId, customerId, divisionId)
    
    #puts "Debug: " + rec.inspect
    #puts "owner_id: " + rec.owner_id.to_s
    
    rec.save
    
  end

  def self.resolve_base_owner(tenantId, organizationId, customerId, divisionId)
    if (!divisionId.nil?)
      #puts "Using division"
      return DivisionOrg.find_by_legacy_id(divisionId).baseOrg.id
    end
    
    if (!customerId.nil?)
      #puts "Using customer"
      return CustomerOrg.find_by_legacy_id(customerId).baseOrg.id
    end

    if (!organizationId.nil?)
      #puts "Using Org"
      return BaseOrg.find(organizationId).id
    end
    
    #puts "Using Tenant"
    return Tenant.find(tenantId).primaryOrg.id
  end
end