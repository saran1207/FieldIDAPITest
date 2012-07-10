require 'column_mapping'

class UpdateRemainingColumnMappings < ActiveRecord::Migration

  def self.up
    ColumnMapping.update_all(
        { :sort_expression => "assetStatus.name,asset.assetStatus.name", :join_expression => "assetStatus,asset.assetStatus"},
        { :name => "event_search_assetstatus" } )

    ColumnMapping.update_all(
        { :path_expression => "performedBy.displayName", :join_expression => nil},
        { :name => "event_search_performed_by" } )

    ColumnMapping.update_all(
        { :join_expression => nil},
        { :name => "event_search_comments" } )

    ColumnMapping.update_all(
        { :path_expression => "type.group.name"},
        { :name => "event_search_eventtypegroup" } )

  end

  def self.down
    ColumnMapping.update_all(
        { :sort_expression => "event.assetStatus.name,asset.assetStatus.name", :join_expression => "event.assetStatus,asset.assetStatus"},
        { :name => "event_search_assetstatus" } )

    ColumnMapping.update_all(
        { :path_expression => "event.performedBy.displayName", :join_expression => "event"},
        { :name => "event_search_performed_by" } )

    ColumnMapping.update_all(
        { :join_expression => "event"},
        { :name => "event_search_comments" } )

    ColumnMapping.update_all(
        { :path_expression => "eventType.group.name"},
        { :name => "event_search_eventtypegroup" } )

  end

end