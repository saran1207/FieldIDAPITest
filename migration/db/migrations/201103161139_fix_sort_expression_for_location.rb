class FixSortExpressionForLocation < ActiveRecord::Migration

  def self.up
    execute ("update system_column_mappings set sort_expression = 'advancedLocation.freeformLocation' where path_expression = 'advancedLocation.fullName'")
  end

  def self.down
  end
  
end