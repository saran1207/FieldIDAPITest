class AddMoreWidgetConfigTables < ActiveRecord::Migration

  def self.up

    #clear out existing data to avoid silly migration since no release yet
    execute("delete from dashboard_columns_widget_definitions")
    execute("delete from widget_definitions")

    create_table "widget_configurations_assets_status", :primary_key => "id" do |t|
      t.integer :org_id
      t.string :date_range
    end
    
    add_foreign_key(:widget_configurations_assets_status, :widget_configurations, :source_column => :id, :foreign_column => :id, :name => "fk_assets_status_config_widget_configs")
    add_foreign_key(:widget_configurations_assets_status, :org_base, :source_column => :org_id, :foreign_column => :id, :name => "fk_configs_assets_status_widget_orgs")
    

    create_table "widget_configurations_completed_events", :primary_key => "id" do |t|
      t.integer :org_id
      t.string :date_range
    end
    
    add_foreign_key(:widget_configurations_completed_events, :widget_configurations, :source_column => :id, :foreign_column => :id, :name => "fk_completed_events_config_widget_configs")
    add_foreign_key(:widget_configurations_completed_events, :org_base, :source_column => :org_id, :foreign_column => :id, :name => "fk_configs_completed_events_widget_orgs")
    

    create_table "widget_configurations_upcoming_events", :primary_key => "id" do |t|
      t.integer :org_id
      t.string :date_range
    end

    add_foreign_key(:widget_configurations_upcoming_events, :widget_configurations, :source_column => :id, :foreign_column => :id, :name => "fk_upcoming_events_config_widget_configs")
    add_foreign_key(:widget_configurations_upcoming_events, :org_base, :source_column => :org_id, :foreign_column => :id, :name => "fk_configs_upcoming_events_widget_orgs")
    
  end

  def self.down
    drop_table :widget_configurations_assets_status
    drop_table :widget_configurations_completed_events
    drop_table :widget_configurations_upcoming_events
  end

end