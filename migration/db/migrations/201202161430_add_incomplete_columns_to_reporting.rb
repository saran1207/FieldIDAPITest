require 'column_mapping'
require 'system_column_mapping'
require 'custom_column_mapping'
require 'column_mapping_group'

class AddIncompleteColumnsToReporting < ActiveRecord::Migration

  def self.up
    event_details_group = ColumnMappingGroup.find(:first, :conditions => {:report_type => "EVENT", :group_key => "event_details"})

    create_mapping(event_details_group, "label.event_status", "event_search_event_status", "status", true, "com.n4systems.fieldid.viewhelpers.handlers.EnumHandler", 1165)
    create_mapping(event_details_group, "label.scheduleddate", "event_search_scheduled_date", "nextDate", true, nil, 10105)
    create_mapping(event_details_group, "label.daysPastDue", "event_search_days_past_due", "daysPastDue", false, nil, 10112)

    custom_event_columns = CustomColumnMapping.find(:all, :conditions=> { :report_type => "EVENT", :category => "EVENT" })
    custom_event_columns.each do |column|
      column.column_mapping.path_expression = "event."+column.column_mapping.path_expression
      column.column_mapping.save
    end
  end

  def self.down
    delete_mapping("event_search_event_status")
    delete_mapping("event_search_scheduled_date")
    delete_mapping("event_search_days_past_due")

    custom_event_columns = CustomColumnMapping.find(:all, :conditions=> { :report_type => "EVENT", :category => "EVENT" })
    custom_event_columns.each do |column|
      column.column_mapping.path_expression = column.column_mapping.path_expression["event".size+1,column.column_mapping.path_expression.size]
      column.column_mapping.save
    end
  end

  def self.delete_mapping(name)
    mappings = ColumnMapping.find(:all, :conditions => { :name => name })
    mappings.each do |mapping|
      SystemColumnMapping.delete_all(:column_id => mapping.id)
      ColumnMapping.delete(mapping.id)
    end
  end

  def self.create_mapping(group, label, name, path, sortable, output_handler, default_order)
    mapping = SystemColumnMapping.new

    column_mapping = ColumnMapping.new
    column_mapping.created = Time.now
    column_mapping.modified = Time.now
    column_mapping.label = label
    column_mapping.path_expression = path
    column_mapping.sortable = sortable
    column_mapping.output_handler = output_handler
    column_mapping.default_order = default_order
    column_mapping.column_mapping_group = group
    column_mapping.name = name

    mapping.column_mapping = column_mapping
    mapping.save
  end

end