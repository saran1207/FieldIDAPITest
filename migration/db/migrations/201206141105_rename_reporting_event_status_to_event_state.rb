require 'column_mapping'

class RenameReportingEventStatusToEventState < ActiveRecord::Migration

  def self.up
    rename_column(:saved_reports, :eventStatus, :eventState)

    column = ColumnMapping.find(:first, :conditions => {:label => 'label.event_status'})
    column.label = 'label.event_state'
    column.name =  'event_search_event_state'
    column.save

  end

  def self.down
    rename_column(:saved_reports, :eventState, :eventStatus)

    column = ColumnMapping.find(:first, :conditions => {:label => 'label.event_state'})
    column.label = 'label.event_status'
    column.name =  'event_search_event_status'
    column.save
  end

end