class ColumnMappingGroup < ActiveRecord::Base
  set_table_name :column_mapping_groups
  has_many :column_mappings, :foreign_key => 'group_id',     :class_name => 'ColumnMapping'
end