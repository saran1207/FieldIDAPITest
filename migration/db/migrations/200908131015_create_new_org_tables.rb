require "tenant"

class CreateNewOrgTables < ActiveRecord::Migration
  
  def self.up
    Tenant.transaction do
      
      create_table :org_base do |t|
        create_entity_with_tenant_fields_on(t)
        t.string    :name, :null => false
        t.integer   :addressinfo_id
      end
      
      create_foreign_keys_for_entity_with_tenant(:org_base)
      add_foreign_key(:org_base, :addressinfo,  :source_column => :addressinfo_id, :foreign_column => :id)
      
      create_table :org_primary, :id => false do |t|
        t.integer :org_id, :null => false
        t.integer :asset_limit, :null => false
        t.integer :diskspace_limit, :null => false
        t.integer :user_limit, :null => false
        t.string  :serialnumberformat, :null => false
        t.boolean :usingserialnumber, :null => false
        t.string  :website,:limit => 2056
        t.string  :certificatename
        t.string  :defaulttimezone, :null => false
        t.string  :dateformat, :null => false
      end
      
      execute("ALTER TABLE org_primary ADD PRIMARY KEY (org_id)")
      add_foreign_key(:org_primary, :org_base,  :source_column => :org_id, :foreign_column => :id)
      
      create_table :org_secondary, :id => false do |t|
        t.integer :org_id, :null => false
        t.integer :primaryorg_id, :null => false
        t.string  :certificatename
        t.string  :defaulttimezone, :null => false
      end
      
      execute("ALTER TABLE org_secondary ADD PRIMARY KEY (org_id)")
      add_foreign_key(:org_secondary, :org_base,  :source_column => :org_id, :foreign_column => :id)
      add_foreign_key(:org_secondary, :org_primary,  :source_column => :primaryorg_id, :foreign_column => :org_id, :name => :fk_org_secondary_primaryorg)
  
      
      create_table :org_customer, :id => false do |t|
        t.integer :org_id, :null => false
        t.integer :parent_id, :null => false
      end
      
      execute("ALTER TABLE org_customer ADD PRIMARY KEY (org_id)")
      add_foreign_key(:org_customer, :org_base,  :source_column => :org_id, :foreign_column => :id)
      add_foreign_key(:org_customer, :org_base,  :source_column => :parent_id, :foreign_column => :id, :name => :fk_org_customer_parent)
      
      
      create_table :org_division, :id => false do |t|
        t.integer :org_id, :null => false
        t.integer :parent_id, :null => false
      end
      
      execute("ALTER TABLE org_division ADD PRIMARY KEY (org_id)")
      add_foreign_key(:org_division, :org_base,  :source_column => :org_id, :foreign_column => :id)
      add_foreign_key(:org_division, :org_customer,  :source_column => :parent_id, :foreign_column => :org_id, :name => :fk_org_division_parent)
  
  
      create_table :org_extendedfeatures, :id => false do |t|
        t.integer :org_id, :null => false
        t.string :feature, :null => false
      end
    
      add_foreign_key(:org_extendedfeatures, :org_primary,  :source_column => :org_id, :foreign_column => :org_id)
  
    end
    
  end
  
  def self.down
    
  end
  
end