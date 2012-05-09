require 'column_mapping'

class UpdateEventDateColumn < ActiveRecord::Migration

  def self.up
    ColumnMapping.update_all( { :path_expression => 'completedDate', :join_expression => nil }, { :name => 'event_search_date_performed' } )
  end

  def self.down
    ColumnMapping.update_all( { :path_expression => 'event.date', :join_expression => 'event' }, { :name => 'event_search_date_performed' } )
  end

end