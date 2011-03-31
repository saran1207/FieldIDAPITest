require 'column_mapping'
require 'system_column_mapping'
require 'column_mapping_group'
require 'saved_report'

class MigrateSavedReportSortColumnsToIds < ActiveRecord::Migration

  def self.up
    add_column(:savedreports, :sort_column_id, :integer)
    add_foreign_key(:savedreports, :column_mappings, :source_column => :sort_column_id, :foreign_column => :id)

    default_sort_column = ColumnMapping.find(:first, :conditions => {:name => 'event_search_date_performed'})

    event_column_groups = ColumnMappingGroup.find(:all, :conditions => { :report_type => "EVENT" }).collect { |e| e.id }

    saved_reports = SavedReport.find(:all)

    saved_reports.each do |report|
      sort_expr = report.sortcolumn
      mapping = ColumnMapping.find_by_sql("select c.* from system_column_mappings s, column_mappings c where s.column_id = c.id and c.group_id in (#{event_column_groups.join(',')}) and (c.sort_expression='#{sort_expr}' or (c.sort_expression is null and c.path_expression='#{sort_expr}'))").first
      if mapping.nil?
        report.sort_column_id = default_sort_column.id
      else
        report.sort_column_id = mapping.id
      end
      report.save
    end
  end

  def self.down
    execute "alter table savedreports drop foreign key fk_savedreports_column_mappings"
    execute "alter table savedreports drop key fk_savedreports_column_mappings"
    remove_column(:savedreports, :sort_column_id)
  end

end