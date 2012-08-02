require 'column_mapping'

class FixTemplateCustomColumns < ActiveRecord::Migration

  def self.up
    custom_mappings = ColumnMapping.find_by_sql("select * from column_mappings where path_expression like 'event.infoOptionMap%' ")

    custom_mappings.each do |mapping|
      mapping.path_expression = mapping.path_expression[6..-1]
      mapping.save
    end
  end

  def self.down
    custom_mappings = ColumnMapping.find_by_sql("select * from column_mappings where path_expression like 'infoOptionMap%' ")

    custom_mappings.each do |mapping|
      mapping.path_expression = "event."+mapping.path_expression
      mapping.save
    end
  end

end