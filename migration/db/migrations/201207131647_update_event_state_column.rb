require 'column_mapping'

class UpdateEventStateColumn < ActiveRecord::Migration

  def self.up
    ColumnMapping.update_all(
        { :path_expression => "eventState" },
        { :name => "event_search_event_state" } )
  end

  def self.down
    ColumnMapping.update_all(
        { :path_expression => "status" },
        { :name => "event_search_event_state" } )
  end

end