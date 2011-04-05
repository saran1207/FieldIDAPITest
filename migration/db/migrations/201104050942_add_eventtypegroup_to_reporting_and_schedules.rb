require 'column_mapping_group'
require 'column_mapping'
require 'system_column_mapping'

class AddEventtypegroupToReportingAndSchedules < ActiveRecord::Migration

  def self.up
  
  	ColumnMapping.find(:all, :conditions => "group_id IS NOT NULL").each do | column |
  		column.default_order = column.default_order * 10
  		column.save
  	end
  
    grp_event_event_info = ColumnMappingGroup.find(:first, :conditions => {:group_key => "event_details", :report_type => "EVENT"})
    grp_schedule_event_detail = ColumnMappingGroup.find(:first, :conditions => {:group_key => "event_details", :report_type => "SCHEDULE"})
    
    event_eventtypegroup_column = create_column_mapping({ :label => "label.eventtypegroup", :path_expression => "type.group.name", :sortable => true, :default_order => 10115, :group => grp_event_event_info, :name => "event_search_eventtypegroup"})
    sched_eventtypegroup_column = create_column_mapping({ :label => "label.eventtypegroup", :path_expression => "eventType.group.name", :sortable => true, :default_order => 10410, :group => grp_schedule_event_detail, :name => "event_schedule_eventtypegroup"})

	event_eventtypegroup_column.save
	sched_eventtypegroup_column.save
  
  end

  def self.down

  end
  
  def self.create_column_mapping(opts)
    mapping = SystemColumnMapping.new

    column_mapping = ColumnMapping.new
    column_mapping.created = Time.now
    column_mapping.modified = Time.now
    column_mapping.label = opts[:label]
    column_mapping.path_expression = opts[:path_expression]
    column_mapping.sort_expression = opts[:sort_expression]
    column_mapping.sortable = opts[:sortable]
    column_mapping.output_handler = opts[:output_handler]
    column_mapping.default_order = opts[:default_order]
    column_mapping.column_mapping_group = opts[:group]
    column_mapping.name = opts[:name]
    column_mapping.required_extended_feature = opts[:required_extended_feature]

    mapping.column_mapping = column_mapping
    return mapping
  end

end 