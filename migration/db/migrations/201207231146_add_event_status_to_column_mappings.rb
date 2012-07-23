require 'column_mapping_group'
require 'column_mapping'
require 'system_column_mapping'

class AddEventStatusToColumnMappings < ActiveRecord::Migration
  def self.up

    grp_event_event_details = ColumnMappingGroup.find(:first, :conditions => {:group_key => 'event_details', :report_type=> 'EVENT'})

    mapping = SystemColumnMapping.new

    column_mapping = ColumnMapping.new
    column_mapping.created = Time.now
    column_mapping.modified = Time.now
    column_mapping.label = 'label.event_status'
    column_mapping.path_expression = 'eventStatus.name'
    column_mapping.sortable = true
    column_mapping.default_order = 10125
    column_mapping.column_mapping_group = grp_event_event_details
    column_mapping.name = 'event_search_event_status'

    mapping.column_mapping = column_mapping

    column_mapping.save
    mapping.save

  end

  def self.down
    column = ColumnMapping.find(:first, :conditions => {:name => 'event_search_event_status'})
    mapping = SystemColumnMapping.find(column_id=> column.id)

    SystemColumnMapping.delete(mapping.id)
    ColumnMapping.delete(column.id)
  end

end