class AddEventCompletenessWidgetConfigTables < ActiveRecord::Migration

  def self.up

    create_table "widget_configurations_event_completeness", :primary_key => "id" do |t|
      t.integer :org_id
      t.string :date_range
    end

    add_foreign_key(:widget_configurations_event_completeness, :widget_configurations, :source_column => :id, :foreign_column => :id, :name => "fk_event_completeness_config_widget_configs")
    add_foreign_key(:widget_configurations_event_completeness, :org_base, :source_column => :org_id, :foreign_column => :id, :name => "fk_configs_event_completeness_widget_orgs")
  end

  def self.down
    drop_table :widget_configurations_event_completeness
  end

end