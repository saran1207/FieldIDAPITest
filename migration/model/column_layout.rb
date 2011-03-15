require 'active_column_mapping'

class ColumnLayout < ActiveRecord::Base
  set_table_name :column_layouts
  belongs_to  :sort_column, :foreign_key => 'sort_column_id', :class_name => 'ColumnMapping'
end