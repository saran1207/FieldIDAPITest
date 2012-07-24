require 'column_mapping'

class ChangeJobAssignmentSearchToUseEventsTable < ActiveRecord::Migration

  def self.up
    ColumnMapping.update_all( {:path_expression=>"type.name"}, {:name=>"schedule_to_job_event_type"} )
    ColumnMapping.update_all( {:path_expression=>"eventState"}, {:name=>"schedule_to_job_status"} )
  end

  def self.down
    ColumnMapping.update_all( {:path_expression=>"eventType.name"}, {:name=>"schedule_to_job_event_type"} )
    ColumnMapping.update_all( {:path_expression=>"status"}, {:name=>"schedule_to_job_status"} )
  end

end