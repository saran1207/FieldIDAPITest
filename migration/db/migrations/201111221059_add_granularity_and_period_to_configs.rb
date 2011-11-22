class AddGranularityAndPeriodToConfigs < ActiveRecord::Migration

  def self.up
    add_column(:widget_configurations_assets_identified, :granularity, :string, :length => 50, :null => false)
    add_column(:widget_configurations_completed_events, :granularity, :string, :length => 50, :null => false)
    add_column(:widget_configurations_upcoming_events, :period, :integer, :null => false)

    execute "update widget_configurations_assets_identified set granularity = 'WEEK'"
    execute "update widget_configurations_completed_events set granularity = 'WEEK'"
    execute "update widget_configurations_upcoming_events set period = 30"
  end

  def self.down
    remove_column(:widget_configurations_assets_identified, :granularity)
    remove_column(:widget_configurations_completed_events, :granularity)
    remove_column(:widget_configurations_upcoming_events, :period)
  end

end