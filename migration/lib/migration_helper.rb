module MigrationHelpers
  
  def create_abstract_entity_fields_on( table )
    table.timestamp :created, :null => false
    table.timestamp :modified, :null => false
    table.integer :modifiedby
  end
  def create_entity_with_tenant_fields_on( table )
    create_abstract_entity_fields_on(table)
    table.integer :r_tenant, :null => false
  end
  
  def create_foreign_keys_for_entity_with_tenant(table)
    add_foreign_key(table, :users,  :source_column => :modifiedby, :foreign_key => :uniqueid)
    add_foreign_key(table, :organization, :source_column => :r_tenant, :foreign_column => :id)
  end
  
end
