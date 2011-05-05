require 'column_mapping'

class FixJoinExpressionOfEventResult < ActiveRecord::Migration

  def self.up
    ColumnMapping.update_all({ :join_expression => nil }, {:path_expression=>"status.displayName"})
  end

  def self.down
    ColumnMapping.update_all({ :join_expression => "book" }, {:path_expression=>"status.displayName"})
  end

end
