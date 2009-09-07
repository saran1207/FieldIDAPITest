require "tenant"

class MigrateTenantFields < ActiveRecord::Migration
  
  def self.up
    Tenant.transaction do
      tenantTables = [
        'alertstatus',
        'autoattributecriteria',
        'autoattributedefinition',
        'catalogs',
        'commenttemplate',
        'criteria',
        'criteriaresults',
        'criteriasections',
        'customers',
        'divisions',
        'eulaacceptances',
        'fileattachments',
        'findproductoption_manufacture',
        'inspectionbooks',
        'inspectiongroups',
        'inspections',
        'inspectionschedules',
        'inspectiontypegroups',
        'inspectiontypes',
        'jobsites',
        'legacybuttonstatemappings',
        'lineitems',
        'notificationsettings',
        'observations',
        'orders',
        'populatorlog',
        'productattachments',
        'productcodemapping',
        'products',
        'productstatus',
        'producttypegroups',
        'producttypes',
        'producttypeschedules',
        'projects',
        'requesttransactions',
        'savedreports',
        'serialnumbercounter',
        'setupdatalastmoddates',
        'states',
        'statesets',
        'tagoptions',
        'userrequest',
        'users' ]
      
      tablesMissingFK = [ 'divisions', 'lineitems', 'productattachments', 'producttypegroups', 'savedreports' ]
      
      tenantTables.each do |table|
        puts table
        
        if (!tablesMissingFK.include?(table))
          drop_foreign_key(table, :organization, :source_column => :r_tenant, :foreign_column => :id)
        end
        
        rename_column(table, :r_tenant, :tenant_id)
        if (tablesMissingFK.include?(table))
          add_foreign_key(table, :tenants,  :source_column => :tenant_id, :foreign_column => :id)
        end
      end
      
    end

  end
  
  def self.down
  end
  
end