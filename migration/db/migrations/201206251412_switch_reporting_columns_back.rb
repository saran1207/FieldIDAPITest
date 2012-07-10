require 'column_mapping'

class SwitchReportingColumnsBack < ActiveRecord::Migration

  def self.up
    ColumnMapping.update_all(
        { :path_expression => "status.displayName", :sort_expression => "status", :join_expression => nil },
        { :name => "event_search_eventresult" } )

    ColumnMapping.update_all(
        { :path_expression => "book.name", :join_expression => "book" },
        { :name => "event_search_eventbook" } )

    ColumnMapping.update_all(
        { :path_expression => "comments" },
        { :name => "event_search_comments" } )

    ColumnMapping.update_all(
        { :path_expression => "proofTestInfo.peakLoad", :join_expression => nil },
        { :name => "event_search_peakload" } )

    ColumnMapping.update_all(
        { :path_expression => "proofTestInfo.duration", :join_expression => nil },
        { :name => "event_search_testduration" } )

    ColumnMapping.update_all(
        { :path_expression => "proofTestInfo.peakLoad", :join_expression => nil },
        { :name => "event_search_peakLoadDuration" } )

    ColumnMapping.update_all(
        { :path_expression => "type.name" },
        { :name => "event_search_eventtype" } )

    ColumnMapping.update_all(
        { :path_expression => "score" },
        { :name => "event_score" } )

  end

  def self.down
    ColumnMapping.update_all(
        { :path_expression => "event.status.displayName", :sort_expression => "event.status", :join_expression => "event" },
        { :name => "event_search_eventresult" } )

    ColumnMapping.update_all(
        { :path_expression => "event.book.name", :join_expression => "event.book" },
        { :name => "event_search_eventbook" } )

    ColumnMapping.update_all(
        { :path_expression => "event.comments" },
        { :name => "event_search_comments" } )

    ColumnMapping.update_all(
        { :path_expression => "event.proofTestInfo.peakLoad", :join_expression => "event" },
        { :name => "event_search_peakload" } )

    ColumnMapping.update_all(
        { :path_expression => "event.proofTestInfo.duration", :join_expression => "event" },
        { :name => "event_search_testduration" } )

    ColumnMapping.update_all(
        { :path_expression => "event.proofTestInfo.peakLoadDuration", :join_expression => "event" },
        { :name => "event_search_peakLoadDuration" } )

    ColumnMapping.update_all(
        { :path_expression => "eventType.name" },
        { :name => "event_search_eventtype" } )

    ColumnMapping.update_all(
        { :path_expression => "event.score" },
        { :name => "event_score" } )

  end

end