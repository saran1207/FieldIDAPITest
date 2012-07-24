require 'column_mapping'

class ModifyEventStatusColumnMapping < ActiveRecord::Migration
  def self.up
    column_mapping = ColumnMapping.find(:first, :conditions => {:name => 'event_search_event_status'})
    column_mapping.join_expression = 'eventStatus'
    column_mapping.save
  end

  def self.down
    column_mapping = ColumnMapping.find(:first, :conditions => {:name => 'event_search_event_status'})
    column_mapping.join_expression = nil;
    column_mapping.save
  end

end