require 'column_mapping'

class FixColumnPathsForScheduleReportingMerge < ActiveRecord::Migration

  def self.up
    prepend_to_paths("event_search_date_performed", "event")
    prepend_to_paths("event_search_eventresult", "event")
    prepend_to_paths("event_search_eventbook", "event")
    prepend_to_paths("event_search_performed_by", "event")
    prepend_to_paths("event_search_comments", "event")
    prepend_to_paths("event_search_customer", "event")
    prepend_to_paths("event_search_division", "event")
    prepend_to_paths("event_search_organization", "event")
    prepend_to_paths("event_search_assetstatus", "event")
    prepend_to_paths("event_search_peakload", "event")
    prepend_to_paths("event_search_testduration", "event")
    prepend_to_paths("event_search_peakloadduration", "event")

    ColumnMapping.update_all({ :path_expression => "eventType.name" },  { :name => "event_search_eventtype" })
    ColumnMapping.update_all({ :path_expression => "eventType.group.name" },  { :name => "event_search_eventtypegroup" })
  end

  def self.down
    remove_from_paths("event_search_date_performed", "event")
    remove_from_paths("event_search_eventresult", "event")
    remove_from_paths("event_search_eventbook", "event")
    remove_from_paths("event_search_performed_by", "event")
    remove_from_paths("event_search_comments", "event")
    remove_from_paths("event_search_customer", "event")
    remove_from_paths("event_search_division", "event")
    remove_from_paths("event_search_organization", "event")
    remove_from_paths("event_search_assetstatus", "event")
    remove_from_paths("event_search_peakload", "event")
    remove_from_paths("event_search_testduration", "event")
    remove_from_paths("event_search_peakloadduration", "event")

    ColumnMapping.update_all({ :path_expression => "type.name" },  { :name => "event_search_eventtype" })
    ColumnMapping.update_all({ :path_expression => "type.group.name" },  { :name => "event_search_eventtypegroup" })
  end


  def self.prepend_to_paths(column_name, path_to_prepend)
    column = ColumnMapping.find_by_name(column_name)
    if !column.path_expression.nil?
      column.path_expression = path_to_prepend + "." + column.path_expression
    end
    if !column.sort_expression.nil?
      column.sort_expression = path_to_prepend + "." + column.sort_expression
    end
    if column.join_expression.nil?
      column.join_expression = path_to_prepend
    else
      column.join_expression = path_to_prepend + "." + column.join_expression
    end
    column.save
  end

  def self.remove_from_paths(column_name, path_to_remove)
    column = ColumnMapping.find_by_name(column_name)
    if !column.path_expression.nil?
      column.path_expression = column.path_expression[path_to_remove.size+1,column.path_expression.size]
    end
    if !column.sort_expression.nil?
      column.sort_expression = column.sort_expression[path_to_remove.size+1,column.sort_expression.size]
    end
    if !column.join_expression.nil?
      if column.join_expression == path_to_remove
        column.join_expression = nil
      else
        column.join_expression = column.join_expression[path_to_remove.size+1,column.join_expression.size]
      end
    end
    column.save
  end

end