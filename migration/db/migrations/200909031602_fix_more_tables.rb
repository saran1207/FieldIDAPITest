class FixMoreTables < ActiveRecord::Migration
  
	def self.up
    
    rename_column(:savedreports, :owner_uniqueid, :user_id)
    add_foreign_key(:savedreports, :users, :source_column => :user_id, :foreign_column => :uniqueid)
    
    rename_column(:productserialextension, :r_tenant, :tenant_id)
    add_foreign_key(:productserialextension, :tenants,  :source_column => :tenant_id, :foreign_column => :id)

  end
	
	def self.down
  end
end