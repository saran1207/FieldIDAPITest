require 'column_mapping'

class FixSortExpressionOfEventResult < ActiveRecord::Migration

  def self.up
    result_column = ColumnMapping.find(:first, :conditions => {:name=>"event_search_eventresult"})
    result_column.sort_expression = "status"
    result_column.save
  end

  def self.down
    result_column = ColumnMapping.find(:first, :conditions => {:name=>"event_search_eventresult"})
    result_column.sort_expression = nil
    result_column.save
  end

end