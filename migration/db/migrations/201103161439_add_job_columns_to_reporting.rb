require 'column_mapping_group'
require 'system_column_mapping'

class AddJobColumnsToReporting < ActiveRecord::Migration

  def self.up
    group = ColumnMappingGroup.find(:first, :conditions => {:group_key => 'event_details', :report_type => "EVENT"})

    job_name_column = create_column_mapping({ :label => "label.projectid", :path_expression => "schedule.project.projectID", :sortable => true, :default_order => 100, :group => group, :name => "event_job_id", :required_extended_feature => "Projects" })
    job_id_column = create_column_mapping({ :label => "label.projectname", :path_expression => "schedule.project.name", :sortable => true, :default_order => 101, :group => group, :name => "event_job_name", :required_extended_feature => "Projects" })

    job_name_column.save
    job_id_column.save
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
    column_mapping.default_order = 1000+opts[:default_order]
    column_mapping.column_mapping_group = opts[:group]
    column_mapping.name = opts[:name]
    column_mapping.required_extended_feature = opts[:required_extended_feature]

    mapping.column_mapping = column_mapping
    return mapping
  end

end