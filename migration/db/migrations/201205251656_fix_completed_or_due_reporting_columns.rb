require 'column_mapping'

class FixCompletedOrDueReportingColumns < ActiveRecord::Migration

  def self.up
    ColumnMapping.update_all(
        { :output_handler => "com.n4systems.fieldid.viewhelpers.handlers.completedordue.ReportingAssetStatusHandler", :sort_expression => "event.assetStatus.name,asset.assetStatus.name", :path_expression=>"", :join_expression=>"event.assetStatus,asset.assetStatus" },
          { :name => "event_search_assetstatus" } )

    ColumnMapping.update_all(
        { :output_handler => "com.n4systems.fieldid.viewhelpers.handlers.completedordue.ReportingAssignedToHandler", :path_expression => "" },
          { :name => "event_search_assignedto" } )
  end

  def self.down
    ColumnMapping.update_all(
        { :output_handler => nil, :sort_expression => nil, :path_expression => "event.assetStatus.name", :join_expression => "event.assetStatus" },
          { :name => "event_search_assetstatus" } )

    ColumnMapping.update_all(
        { :output_handler => "com.n4systems.fieldid.viewhelpers.handlers.AssignedToUpdateHandler", :path_expression => "event.assignedTo" },
          { :name => "event_search_assignedto" } )
  end

end