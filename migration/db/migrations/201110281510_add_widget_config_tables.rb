class AddWidgetConfigTables < ActiveRecord::Migration

  def self.up

    #clear out existing data to avoid silly migration since no release yet
    execute("delete from dashboard_columns_widget_definitions")
    execute("delete from widget_definitions")

    add_column(:widget_definitions, :config_id, :integer, :null=> false)
    remove_column(:widget_definitions, :name)

    create_table "widget_configurations", :primary_key => "id" do |t|
      create_abstract_entity_fields_on(t)
      t.string  :name
    end

    create_table "widget_configurations_event_kpi", :primary_key => "id" do |t|
    end

    create_table "widget_configurations_event_kpi_orgs", :primary_key => "id" do |t|
      t.integer :config_id
      t.integer :org_id
      t.integer :orderIdx
    end

    add_foreign_key(:widget_configurations_event_kpi, :widget_configurations, :source_column => :id, :foreign_column => :id, :name => "fk_event_kpi_config_widget_configs")

    add_foreign_key(:widget_configurations_event_kpi_orgs, :widget_configurations_event_kpi, :source_column => :config_id, :foreign_column => :id, :name => "fk_configs_event_kpi_widget_configs")
    add_foreign_key(:widget_configurations_event_kpi_orgs, :org_base, :source_column => :org_id, :foreign_column => :id, :name => "fk_configs_event_kpi_widget_orgs")
  end

  def self.down

    drop_table :widget_configurations_event_kpi_orgs
    drop_table :widget_configurations_event_kpi
    drop_table :widget_configurations

    remove_column(:widget_definitions, :config_id)
    add_column(:widget_definitions, :name, :string)
  end

end