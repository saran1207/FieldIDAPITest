module MigrationHelpers
  def foreign_key(from_table, from_column, to_table, id_column = "uniqueid")
    constraint_name = "fk_#{from_table}_#{from_column}" 

    execute %{alter table #{from_table}
              add constraint #{constraint_name}
              foreign key (#{from_column})
              references #{to_table}(#{id_column})}
              
    add_index(from_table, from_column);
  end
  
  def drop_foreign_key(from_table, from_column)
    constraint_name = "fk_#{from_table}_#{from_column}" 

    execute %{alter table #{from_table}
              drop constraint #{constraint_name} }
    remove_index(from_table, from_column);
  end
  
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
    foreign_key(table, :modifiedby, :users, :uniqueid)
    foreign_key(table, :r_tenant, :organization, :id)
  end
  
  def add_null_constriant(table, column)
    execute("ALTER TABLE " + table.to_s + " ALTER COLUMN " + column.to_s + " SET NOT NULL")
  end
  
  def remove_null_constriant(table, column)
    execute("ALTER TABLE " + table.to_s + " ALTER COLUMN " + column.to_s + " DROP NOT NULL")
  end
  
  
end
