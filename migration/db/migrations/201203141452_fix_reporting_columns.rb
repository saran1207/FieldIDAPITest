require 'column_mapping'

class FixReportingColumns < ActiveRecord::Migration

  def self.up
    ColumnMapping.update_all({:path_expression=>"event.score", :join_expression=> "event" },  { :name => "event_score" })

    ColumnMapping.update_all({:path_expression=>"project.projectID", :join_expression=> "project" },  { :name => "event_job_id" })
    ColumnMapping.update_all({:path_expression=>"project.name", :join_expression=> "project" },  { :name => "event_job_name" })

    ColumnMapping.update_all({:path_expression=>"event.assignedTo"},  { :name => "event_search_assignedto" })
  end

  def self.down
    ColumnMapping.update_all({:path_expression=>"score", :join_expression=> nil },  { :name => "event_score" })

    ColumnMapping.update_all({:path_expression=>"schedule.project.projectID", :join_expression=> "schedule.project" },  { :name => "event_job_id" })
    ColumnMapping.update_all({:path_expression=>"schedule.project.name", :join_expression=> "schedule.project" },  { :name => "event_job_name" })

    ColumnMapping.update_all({:path_expression=>"assignedTo"},  { :name => "event_search_assignedto" })
  end

end