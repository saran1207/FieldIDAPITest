class AddWidgetTables < ActiveRecord::Migration

  def self.up

    create_table "dashboard_layouts", :primary_key => "id" do |t|
      create_entity_with_tenant_fields_on(t)
      t.integer :user_id, :null => false
    end

    create_table "dashboard_columns", :primary_key => "id" do |t|
      create_abstract_entity_fields_on(t)
    end

    create_table "widget_definitions", :primary_key => "id" do |t|
      create_abstract_entity_fields_on(t)
      t.string  :widget_type
      t.string  :name
    end

    create_table "dashboard_layouts_dashboard_columns", :primary_key => "id" do |t|
      t.integer :dashboard_layout_id, :null => false
      t.integer :dashboard_column_id, :null => false
      t.integer :orderIdx, :null => false
    end

    create_table "dashboard_columns_widget_definitions", :primary_key => "id" do |t|
      t.integer :dashboard_column_id, :null => false
      t.integer :widget_definition_id, :null => false
      t.integer :orderIdx, :null => false
    end

    add_foreign_key(:dashboard_layouts, :users, :source_column => :user_id, :foreign_column => :id, :name => "fk_dashboard_layouts_users")

    add_foreign_key(:dashboard_layouts_dashboard_columns, :dashboard_layouts, :source_column => :dashboard_layout_id, :foreign_column => :id, :name => "fk_dashboard_layouts_dashboard_columns_layouts")
    add_foreign_key(:dashboard_layouts_dashboard_columns, :dashboard_columns, :source_column => :dashboard_column_id, :foreign_column => :id, :name => "fk_dashboard_layouts_dashboard_columns_columns")

    add_foreign_key(:dashboard_columns_widget_definitions, :dashboard_columns, :source_column => :dashboard_column_id, :foreign_column => :id, :name => "fk_columns_widget_definitions_columns")
    add_foreign_key(:dashboard_columns_widget_definitions, :widget_definitions, :source_column => :widget_definition_id, :foreign_column => :id, :name => "fk_columns_widget_definitions_definitions")
  end

  def self.down
    drop_table "dashboard_layouts_dashboard_columns"
    drop_table "dashboard_columns_widget_definitions"

    drop_table "widget_definitions"
    drop_table "dashboard_layouts"
    drop_table "dashboard_columns"
  end

end