require 'column_mapping'

class FixSortingOfLocationField < ActiveRecord::Migration

  def self.up
    ColumnMapping.update_all({:sort_expression => "advancedLocation.predefinedLocation.id,advancedLocation.freeformLocation"}, {:name => "event_search_location"})
    ColumnMapping.update_all({:sort_expression => "advancedLocation.predefinedLocation.id,advancedLocation.freeformLocation"}, {:name => "event_schedule_location"})
    ColumnMapping.update_all({:sort_expression => "advancedLocation.predefinedLocation.id,advancedLocation.freeformLocation"}, {:name => "asset_search_location"})
    ColumnMapping.update_all({:sort_expression => "advancedLocation.predefinedLocation.id,advancedLocation.freeformLocation"}, {:name => "schedule_to_job_location"})
  end

  def self.down
    ColumnMapping.update_all({:sort_expression => "advancedLocation.freeformLocation"}, {:name => "event_search_location"})
    ColumnMapping.update_all({:sort_expression => "advancedLocation.freeformLocation"}, {:name => "event_schedule_location"})
    ColumnMapping.update_all({:sort_expression => "advancedLocation.freeformLocation"}, {:name => "asset_search_location"})
    ColumnMapping.update_all({:sort_expression => "advancedLocation.freeformLocation"}, {:name => "schedule_to_job_location"})
  end

end