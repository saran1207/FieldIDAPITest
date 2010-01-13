module MigrationHelpers
  
  def create_abstract_entity_fields_on( table )
    table.timestamp :created, :null => false
    table.timestamp :modified, :null => false
    table.integer :modifiedby
  end
  
  def create_entity_with_tenant_fields_on( table )
    create_abstract_entity_fields_on(table)
    table.integer :tenant_id, :null => false
  end
  
  def create_entity_with_owner_fields_on(table) 
    create_entity_with_tenant_fields_on(table)
    table.integer :owner_id, :null => false
  end
  
  def create_foreign_keys_for_entity_with_tenant(table)
    create_foreign_keys_for_abstract_entity(table)
    add_foreign_key(table, :tenants, :source_column => :tenant_id, :foreign_column => :id)
  end
  
  def create_foreign_keys_for_entity_with_owner(table)
    create_foreign_keys_for_entity_with_tenant(table)
    add_foreign_key(table, :org_base, :source_column => :owner_id, :foreign_column => :id)
  end
  
  def create_foreign_keys_for_abstract_entity(table)
    add_foreign_key(table, :users,  :source_column => :modifiedby, :foreign_column => :uniqueid)
  end
  
  def add_time_hash_to(args) 
    {:created => Time.new, :modified => Time.new}.merge(args)
  end
end
