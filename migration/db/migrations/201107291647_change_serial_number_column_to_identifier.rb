require 'column_mapping'

class ChangeSerialNumberColumnToIdentifier < ActiveRecord::Migration

  def self.up
    rename_column :assets, :serialnumber, :identifier
    rename_column :assets, :archivedserialnumber, :archivedIdentifier

    ColumnMapping.update_all( { :path_expression => 'identifier' }, { :path_expression => 'serialNumber' } )
    ColumnMapping.update_all( { :path_expression => 'asset.identifier' }, { :path_expression => 'asset.serialNumber' } )
    ColumnMapping.update_all( { :sort_expression => 'asset.identifier' }, { :sort_expression => 'asset.serialNumber' } )

    ColumnMapping.update_all( { :name => 'event_search_identifier' }, { :name => 'event_search_serialnumber' } )
    ColumnMapping.update_all( { :name => 'event_schedule_identifier' }, { :name => 'event_schedule_serialnumber' } )
    ColumnMapping.update_all( { :name => 'asset_search_identifier' }, { :name => 'asset_search_serialnumber' } )
    ColumnMapping.update_all( { :name => 'schedule_to_job_identifier' }, { :name => 'schedule_to_job_serialnumber' } )

    ColumnMapping.update_all( { :output_handler => 'com.n4systems.fieldid.viewhelpers.handlers.EventIdentifierHandler' }, { :output_handler => 'com.n4systems.fieldid.viewhelpers.handlers.EventSerialNumberHandler' } )
  end


  def self.down
    rename_column :assets, :identifier, :serialnumber
    rename_column :assets, :archivedIdentifier, :archivedserialnumber

    ColumnMapping.update_all( { :path_expression => 'serialNumber' }, { :path_expression => 'identifier' } )
    ColumnMapping.update_all( { :path_expression => 'asset.serialNumber' }, { :path_expression => 'asset.identifier' } )
    ColumnMapping.update_all( { :sort_expression => 'asset.serialNumber' }, { :sort_expression => 'asset.identifier' } )

    ColumnMapping.update_all( { :name => 'event_search_serialnumber' }, { :name => 'event_search_identifier' } )
    ColumnMapping.update_all( { :name => 'event_schedule_serialnumber' }, { :name => 'event_schedule_identifier' } )
    ColumnMapping.update_all( { :name => 'asset_search_serialnumber' }, { :name => 'asset_search_identifier' } )
    ColumnMapping.update_all( { :name => 'schedule_to_job_serialnumber' }, { :name => 'schedule_to_job_identifier' } )

    ColumnMapping.update_all( { :output_handler => 'com.n4systems.fieldid.viewhelpers.handlers.EventSerialNumberHandler' }, { :output_handler => 'com.n4systems.fieldid.viewhelpers.handlers.EventIdentifierHandler' } )
  end

end