
class MigrateMissedTenantTables < ActiveRecord::Migration
	def self.up
    drop_foreign_key(:printouts, :organization, :source_column => :tenant_id, :foreign_column => :id)
    add_foreign_key(:printouts, :tenants,  :source_column => :tenant_id, :foreign_column => :id)

    
    
    drop_foreign_key(:inspectionsmaster, :organization, :source_column => :organization_id, :foreign_column => :id)
    add_foreign_key(:inspectionsmaster, :org_base,  :source_column => :organization_id, :foreign_column => :id)
    
    
    add_foreign_key(:products, :org_base,  :source_column => :organization_id, :foreign_column => :id)
    
    drop_foreign_key(:configurations, :organization, :source_column => :tenantid, :foreign_column => :id)
    add_foreign_key(:configurations, :tenants,  :source_column => :tenantid, :foreign_column => :id)
    
    drop_foreign_key(:associatedinspectiontypes, :organization, :source_column => :r_tenant, :foreign_column => :id)
    rename_column(:associatedinspectiontypes, :r_tenant, :tenant_id)
    add_foreign_key(:associatedinspectiontypes, :tenants,  :source_column => :tenant_id, :foreign_column => :id)
    
  end
	
	def self.down
	end
end