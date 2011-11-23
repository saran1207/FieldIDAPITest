class AddGranularityToEventCompletenessConfig < ActiveRecord::Migration

  def self.up
    add_column(:widget_configurations_event_completeness, :granularity, :string, :length => 50, :null => false)
    execute "update widget_configurations_event_completeness set granularity = 'WEEK'"
  end

  def self.down
    remove_column(:widget_configurations_event_completeness, :granularity)
  end

end