class CreateTenantSettingsTable < ActiveRecord::Migration

  def self.up
    create_table :tenant_settings, :primary_key => "id", :force => true do |t|
      create_entity_with_tenant_fields_on(t)
      t.integer "createdby"
      t.integer "maxEmployeeUsers", :null => false
      t.integer "maxLiteUsers", :null => false
      t.integer "maxReadonlyUsers", :null => false
      t.boolean "secondaryOrgsEnabled", :null => false
    end

  	add_foreign_key(:tenant_settings, :users, :source_column => :createdby, :foreign_column => :id)
  end

  def self.down
    drop_table :active_column_mappings
  end

end
