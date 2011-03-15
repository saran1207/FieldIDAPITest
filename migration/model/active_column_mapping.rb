class ActiveColumnMapping < ActiveRecord::Base
  set_table_name :active_column_mappings
  belongs_to  :column_layout, :foreign_key => 'column_layout_id',     :class_name => 'ColumnLayout'
  belongs_to  :column_mapping, :foreign_key => 'mapping_id', :class_name => 'ColumnMapping'
end