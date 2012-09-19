require 'column_mapping'

class AddJoinColumnToPriorityColumn < ActiveRecord::Migration

  def self.up
    ColumnMapping.update_all( {:join_expression => "priority" }, {:name => "event_search_priority" } )
  end

  def self.down
    ColumnMapping.update_all( {:join_expression => nil }, {:name => "event_search_priority" } )
  end

end