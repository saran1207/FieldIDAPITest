class AddTypedOrgConnections < ActiveRecord::Migration
  def self.up
    create_table :typedorgconnections do |t|
      create_entity_with_owner_fields_on(t)
      t.integer :connectedorg_id, :null => false
      t.string :connectiontype, :null => false
      t.integer :orgconnection_id
    end
    
    create_foreign_keys_for_entity_with_owner(:typedorgconnections)
    add_foreign_key(:typedorgconnections, :org_base, :source_column => :connectedorg_id, :foreign_column => :id, :name => "fk_typedorgconnection_connectedorg")
    add_foreign_key(:typedorgconnections, :org_connections, :source_column => :orgconnection_id, :foreign_column => :id)
   
    add_index(:typedorgconnections, [:owner_id, :connectedorg_id, :connectiontype], :unique => true, :name => "index_typedorgconnection_unique")
  end
  
  def self.down
    drop_table :typedorgconnections
  end
end