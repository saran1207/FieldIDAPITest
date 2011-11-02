class AddAssetsIdentifiedWidgetConfigTables < ActiveRecord::Migration

  def self.up

    create_table "widget_configurations_assets_identified", :primary_key => "id" do |t|
      t.integer :org_id
      t.string :date_range
    end

    add_foreign_key(:widget_configurations_assets_identified, :widget_configurations, :source_column => :id, :foreign_column => :id, :name => "fk_assets_identified_config_widget_configs")
    add_foreign_key(:widget_configurations_assets_identified, :org_base, :source_column => :org_id, :foreign_column => :id, :name => "fk_configs_assets_identified_widget_orgs")
  end

  def self.down
    drop_table :widget_configurations_assets_identified
  end

end