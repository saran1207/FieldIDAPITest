class AddDateRangeToEventKpiConfig < ActiveRecord::Migration

  def self.up
  	add_column(:widget_configurations_event_kpi, :date_range, :string, :null => false)  	
 	execute "update widget_configurations_event_kpi set date_range = 'THIS_WEEK'"  
  end

  def self.down
  	remove_column(:widget_configurations_event_kpi, :date_range)
  end

end