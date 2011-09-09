require 'column_mapping_group'
require 'system_column_mapping'

class AddScoreToEvents < ActiveRecord::Migration

  def self.up
    add_column(:events, :score, :decimal, :precision=>15, :scale=>10)

    group = ColumnMappingGroup.find(:first, :conditions => {:group_key => 'event_details', :report_type => "EVENT"})
    score_column = create_column_mapping({ :label => "label.score", :path_expression => "score", :sortable => true, :default_order => 170, :group => group, :name => "event_score", :output_handler => "com.n4systems.fieldid.viewhelpers.handlers.ShowDecimalsOnlyIfTheyExistHandler"})
    score_column.save
  end

  def self.down
    drop_column(:events, :score)
    SystemColumnMapping.delete_all(:label => "label.score")
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