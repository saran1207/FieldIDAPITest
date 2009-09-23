class AddOrgConnectionTable < ActiveRecord::Migration
  def self.up
    
    create_table :org_connections do |t|
      create_abstract_entity_fields_on(t)
      t.integer :vendor_id, :null => false
      t.integer :customer_id, :null => false
    end
    
    create_foreign_keys_for_abstract_entity(:org_connections)
    add_index(:org_connections, [:vendor_id, :customer_id], :unique => true, :name => "idx_org_connections_unique")
    add_foreign_key(:org_connections, :org_base, :source_column => :vendor_id, :foreign_column => :id, :name => "fk_org_connection_vendor")
    add_foreign_key(:org_connections, :org_base, :source_column => :customer_id, :foreign_column => :id, :name => "fk_org_connection_customer")
    
  end
  
  def self.down
    drop_table(:org_connections)
  end
end