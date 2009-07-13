class AddCatalog < ActiveRecord::Migration
  def self.up
    create_table :catalogs do |t|
      create_entity_with_tenant_fields_on(t)
    end
    foreign_key(:catalogs, :r_tenant, :organization, :id)
    add_index(:catalogs, :r_tenant, :unique => true)
    
    create_table :catalogs_producttypes, :id => false do |t|
        t.integer :catalogs_id, :null => false
        t.integer :publishedtypes_id, :null => false 
    end
    foreign_key(:catalogs_producttypes, :catalogs_id, :catalogs, :id)
    foreign_key(:catalogs_producttypes, :publishedtypes_id, :producttypes, :id)
    add_index(:catalogs_producttypes, [:catalogs_id, :publishedtypes_id], :unique => true)
    add_index(:catalogs_producttypes, :publishedtypes_id, :unique => true)
  end
  
  def self.down
    drop_table :catalogs_producttypes
    drop_table :catalogs
  end
end