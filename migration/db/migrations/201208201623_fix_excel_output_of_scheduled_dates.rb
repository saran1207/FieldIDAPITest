require 'column_mapping'

class FixExcelOutputOfScheduledDates < ActiveRecord::Migration

  def self.up
    ColumnMapping.update_all( { :output_handler => 'com.n4systems.fieldid.viewhelpers.handlers.NonConvertingDateTimeHandler' }, { :name => 'event_search_scheduled_date' } )
  end

  def self.down
    ColumnMapping.update_all( { :output_handler => nil }, { :name => 'event_search_scheduled_date' } )
  end

end