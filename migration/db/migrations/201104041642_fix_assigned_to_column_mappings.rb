class FixAssignedToColumnMappings < ActiveRecord::Migration 

  def self.up
    execute("update column_mappings set path_expression = 'asset.assignedUser.displayName' where name='event_schedule_assignedto'")
    execute("update column_mappings set path_expression = 'assignedUser.displayName', output_handler='com.n4systems.fieldid.viewhelpers.handlers.AssignedToHandler' where name='asset_search_assignedto'")
  end

  def self.down
    execute("update column_mappings set path_expression = 'assignedUser.displayName' where name='event_schedule_assignedto'")
    execute("update column_mappings set path_expression = 'assignedUser', output_handler='com.n4systems.fieldid.viewhelpers.handlers.AssignedToUpdateHandler' where name='asset_search_assignedto'")
  end

end