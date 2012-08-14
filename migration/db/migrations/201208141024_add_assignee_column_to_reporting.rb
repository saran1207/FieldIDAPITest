require 'column_mapping'
require 'system_column_mapping'
require 'column_mapping_group'

class AddAssigneeColumnToReporting < ActiveRecord::Migration
  def self.up

    grp_event_event_details = ColumnMappingGroup.find(:first, :conditions => {:group_key => 'event_details', :report_type=> 'EVENT'})

    mapping = SystemColumnMapping.new

    assignee_column = ColumnMapping.new

    assignee_column.column_mapping_group = grp_event_event_details
    assignee_column.default_order = 10145
    assignee_column.created = Time.now
    assignee_column.modified = Time.now
    assignee_column.name="event_search_assignee"
    assignee_column.label="label.assignee"
    assignee_column.sortable = 1
    assignee_column.path_expression = "assignee.fullName"
    assignee_column.join_expression = "assignee,assignee"
    assignee_column.sort_expression = "assignee.firstName,assignee.lastName"
    assignee_column.save

    mapping.column_mapping = assignee_column

    assignee_column.save
    mapping.save

    add_column(:saved_reports, :assignee, :bigint, :limit => 21)

  end

  def self.down
    column = ColumnMapping.find(:first, :conditions => {:name => 'event_search_assignee'})
    mapping = SystemColumnMapping.find_by_column_id(column.id)

    SystemColumnMapping.delete(mapping.id)
    ColumnMapping.delete(column.id)

    remove_column(:saved_reports, :assignee)
  end

end