require 'column_mapping'

class FixSearchJoinColumns < ActiveRecord::Migration

  def self.up
    ColumnMapping.update_all( { :join_expression => 'shopOrder' }, { :name => 'asset_search_order_description' } )
    ColumnMapping.update_all( { :join_expression => 'schedule.project' }, { :name => 'event_job_id' } )
    ColumnMapping.update_all( { :join_expression => 'schedule.project' }, { :name => 'event_job_name' } )
  end

  def self.down
  end

end