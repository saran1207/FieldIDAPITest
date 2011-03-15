class CreateColumnMappingsTables < ActiveRecord::Migration

  def self.up

    create_table :column_mapping_groups, :primary_key => "id", :force => true do |t|
      create_abstract_entity_fields_on(t)
      t.integer "createdby"
      t.string  "label"
      t.string  "group_key"
      t.string "report_type"
      t.integer "ordervalue"
    end

    create_table :column_mappings, :primary_key => "id", :force => true do |t|
      create_abstract_entity_fields_on(t)
      t.integer "createdby"
      t.string  "label"
      t.string  "name"
      t.string  "path_expression"
      t.string  "sort_expression"
      t.string  "required_extended_feature"
      t.boolean "sortable"
      t.string  "output_handler"
      t.integer "default_order"
      t.integer "group_id"
    end

    create_table :system_column_mappings, :primary_key => "id", :force => true do |t|
      t.integer "column_id"
    end

    create_table :custom_column_mappings, :primary_key => "id", :force => true do |t|
      t.integer "column_id"
      t.integer "tenant_id"
      t.string  "category"
      t.string  "report_type"
    end

    create_table :active_column_mappings, :primary_key => "id", :force => true do |t|
      create_entity_with_tenant_fields_on(t)
      t.integer "createdby"
      t.integer  "mapping_id"
      t.integer  "ordervalue"
      t.integer  "column_layout_id"
    end

    # Allow for null tenants to store system defaults
    execute "alter table active_column_mappings change column tenant_id tenant_id bigint(20)"

    create_table :column_layouts, :primary_key => "id", :force => true do |t|
      create_entity_with_tenant_fields_on(t)
      t.integer "createdby"
      t.integer "sort_column_id"
      t.string  "sort_direction"
      t.string  "report_type"
    end

    # Allow for null tenants to store system defaults
    execute "alter table column_layouts change column tenant_id tenant_id bigint(20)"

    add_foreign_key(:system_column_mappings, :column_mappings, :source_column => :column_id, :foreign_column => :id)
    add_foreign_key(:custom_column_mappings, :column_mappings, :source_column => :column_id, :foreign_column => :id)
    add_foreign_key(:custom_column_mappings, :tenants, :source_column => :tenant_id, :foreign_column => :id)

    add_foreign_key(:active_column_mappings, :column_mappings, :source_column => :mapping_id, :foreign_column => :id)
    add_foreign_key(:active_column_mappings, :column_layouts, :source_column => :column_layout_id, :foreign_column => :id)

    add_foreign_key(:column_layouts, :column_mappings, :source_column => :sort_column_id, :foreign_column => :id)
  end

  def self.down
    drop_table :active_column_mappings
    drop_table :system_column_mappings
    drop_table :custom_column_mappings
    drop_table :column_mappings
  end

end
