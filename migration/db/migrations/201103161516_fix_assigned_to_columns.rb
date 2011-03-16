require 'column_mapping_group'
require 'column_mapping'

class FixAssignedToColumns < ActiveRecord::Migration

  def self.up
    ownership_group = ColumnMappingGroup.find(:first, :conditions => {:group_key => "ownership", :report_type => "SCHEDULE"})

    assigned_to_column = ownership_group.column_mappings.select{ |e| e.label == "label.assignedto" }.first
    assigned_to_column.output_handler = "com.n4systems.fieldid.viewhelpers.handlers.AssignedToHandler"
    assigned_to_column.save

    ownership_group = ColumnMappingGroup.find(:first, :conditions => {:group_key => "ownership", :report_type => "ASSET"})
    assigned_to_column.output_handler = "com.n4systems.fieldid.viewhelpers.handlers.AssignedToHandler"
    assigned_to_column.path_expression = "assignedUser.displayName"
    assigned_to_column.save
  end

  def self.down
    
  end

end